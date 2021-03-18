package name.lattuada.trading.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "Get list of orders",
            notes = "Returns a list of orders")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No orders"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<List<Order>> getOrders() {
        try {
            List<Order> orderList = orderRepository.findAll();
            if (orderList.isEmpty()) {
                logger.info("No orders found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.debug("Found {} orders: {}", orderList.size(), orderList);
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get a specific order",
            notes = "Returns specific order given its id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No orders found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<Order> getOrderById(@PathVariable("id") UUID uuid) {
        try {
            Optional<Order> optOrder = orderRepository.findById(uuid);
            return optOrder.map(order -> {
                logger.debug("Order found: {}", order);
                return new ResponseEntity<>(order, HttpStatus.OK);
            }).orElseGet(() -> {
                logger.warn("No order found having id {}", uuid);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            });
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create order",
            notes = "Create a new order. Note: its ID is not mandatory, but it will be automatically generated")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Order created"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<Order> addOrder(@Valid @RequestBody Order order) {
        try {
            Order created = orderRepository.save(order);
            created = orderRepository.getOne(created.getId());
            logger.debug("Added order {}", created);
            //
            List<Order> relatedOrders = orderRepository.findBySecurityIdAndTypeAndFulfilled(created.getSecurityId(),
                    EOrderType.BUY == created.getType() ? EOrderType.SELL : EOrderType.BUY,
                    false);
            manageTrading(created, relatedOrders);
            //
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void manageTrading(Order created, List<Order> relatedOrders) {
        if (!relatedOrders.isEmpty()) {
            logger.info("Found related order(s). Created: {}; related found: {}", created, relatedOrders);
            if (EOrderType.BUY == created.getType()) {
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
            logger.debug("No unfulfilled related orders found for securityId {}", created.getSecurityId());
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
