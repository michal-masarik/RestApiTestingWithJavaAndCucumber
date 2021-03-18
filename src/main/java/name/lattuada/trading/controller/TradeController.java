package name.lattuada.trading.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import name.lattuada.trading.model.Trade;
import name.lattuada.trading.repository.ITradeRepository;
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
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tradeList, HttpStatus.OK);
        } catch (Exception e) {
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
            return optTrade.map(trade -> new ResponseEntity<>(trade, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
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
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(tradeList.get(0), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
