package name.lattuada.trading.controller;

import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.Order;
import name.lattuada.trading.model.Trade;
import name.lattuada.trading.repository.IOrderRepository;
import name.lattuada.trading.repository.ITradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IOrderRepository orderRepository;
    @Autowired
    @Lazy
    ITradeRepository tradeRepository;

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orderList = orderRepository.findAll();
        if (orderList.isEmpty()) {
            logger.info("No orders found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.debug("Found {} orders: {}", orderList.size(), orderList);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") UUID uuid) {
        Optional<Order> optOrder = orderRepository.findById(uuid);
        return optOrder.map(order -> {
            logger.debug("Order found: {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }).orElseGet(() -> {
            logger.warn("No order found having id {}", uuid);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> addOrder(@Valid @RequestBody Order order) {
        try {
            Order created = orderRepository.save(order);
            created = orderRepository.getOne(created.getId());
            logger.debug("Added order {}", created);
            //
            EOrderType createdOrderType = created.getType();
            UUID createdSecurityId = created.getSecurityId();
            List<Order> relatedOrders = orderRepository.findBySecurityIdAndTypeAndFulfilled(createdSecurityId,
                    EOrderType.BUY.equals(createdOrderType) ? EOrderType.SELL : EOrderType.BUY,
                    false);
            if (!relatedOrders.isEmpty()) {
                logger.info("Found related order(s). Created: {}; related found: {}", created, relatedOrders);
                if (EOrderType.BUY.equals(createdOrderType)) {
                    // I created a BUY order and now I found all related SELL order(s)
                    // Find the one with min price
                    Order related = orderRepository.getOne(relatedOrders.stream()
                            .min(Comparator.comparing(Order::getPrice))
                            .orElseThrow(NoSuchElementException::new).getId());
                    if (created.getPrice() >= related.getPrice()) {
                        createTrade(created, related);
                    }
                } else {
                    // I created a SELL order and now I found all related BUY order(s)
                    // For sake of simplicity, I take the first one
                    Order related = orderRepository.getOne(relatedOrders.get(0).getId());
                    if (created.getPrice() <= related.getPrice()) {
                        createTrade(related, created);
                    }
                }
            } else {
                logger.debug("No unfulfilled related orders found for securityId {}", createdSecurityId);
            }
            //
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createTrade(Order buy, Order sell) {
        logger.debug("It's time to trade!");
        // Create trade
        Trade trade = new Trade();
        trade.setPrice(sell.getPrice());
        trade.setQuantity(buy.getQuantity());
        trade.setOrderSellId(sell.getId());
        trade.setOrderBuyId(buy.getId());
        trade = tradeRepository.save(trade);
        logger.info("Trade has been created: {}", trade);
        // Update orders
        buy.setFulfilled(Boolean.TRUE);
        sell.setFulfilled(Boolean.TRUE);
        sell = orderRepository.save(sell);
        logger.info("Sell order has been updated: {}", sell);
        buy = orderRepository.save(buy);
        logger.info("Buy order has bee updated: {}", buy);
    }

}
