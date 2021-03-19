package name.lattuada.trading.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import name.lattuada.trading.model.Trade;
import name.lattuada.trading.repository.ITradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
    private static final String EXCEPTION_CAUGHT = "Exception caught";

    @Autowired
    ITradeRepository tradeRepository;

    @GetMapping()
    @ApiOperation(value = "Get list of trades",
            notes = "Returns a list of trades")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No trades"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<List<Trade>> getTrades() {
        try {
            List<Trade> tradeList = tradeRepository.findAll();
            if (tradeList.isEmpty()) {
                logger.info("No trades found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.debug("Found {} trades: {}", tradeList.size(), tradeList);
            return new ResponseEntity<>(tradeList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(EXCEPTION_CAUGHT, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get a specific trade",
            notes = "Returns specific trade given its id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No trades found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<Trade> getTradeById(@PathVariable("id") UUID uuid) {
        try {
            Optional<Trade> optTrade = tradeRepository.findById(uuid);
            return optTrade.map(trade -> {
                logger.debug("Trade found: {}", trade);
                return new ResponseEntity<>(trade, HttpStatus.OK);
            }).orElseGet(() -> {
                logger.warn("No trade found having id {}", uuid);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            });
        } catch (Exception e) {
            logger.error(EXCEPTION_CAUGHT, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("orderBuyId/{orderBuyId}/orderSellId/{orderSellId}")
    @ApiOperation(value = "Get a specific trade based on the buy and sell order identifiers",
            notes = "Returns specific trade having a given buy order id and a sell order id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No trades found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<Trade> getTradeByBuyAndSellOrderId(@PathVariable("orderBuyId") UUID orderBuyId,
                                                             @PathVariable("orderSellId") UUID orderSellId) {
        try {
            List<Trade> tradeList = tradeRepository.findByOrderBuyIdAndOrderSellId(orderBuyId, orderSellId);
            if (tradeList.isEmpty()) {
                logger.info("No trades found based on buy oder ID {} and sell order id {}", orderBuyId, orderSellId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Trade trade = tradeList.get(0);
                logger.info("Found a trade: {}", trade);
                return new ResponseEntity<>(trade, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(EXCEPTION_CAUGHT, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
