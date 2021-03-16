package name.lattuada.trading.controller;

import name.lattuada.trading.model.Trade;
import name.lattuada.trading.repository.ITradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    ITradeRepository tradeRepository;

    @GetMapping()
    public ResponseEntity<List<Trade>> getTrades() {
        List<Trade> tradeList = tradeRepository.findAll();
        if (tradeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tradeList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Trade> getTradeById(@PathVariable("id") UUID uuid) {
        Optional<Trade> optTrade = tradeRepository.findById(uuid);
        return optTrade.map(trade -> new ResponseEntity<>(trade, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("orderBuyId/{orderBuyId}/orderSellId/{orderSellId}")
    public ResponseEntity<Trade> getTradeByBuyAndSellOrderId(@PathVariable("orderBuyId") UUID orderBuyId,
                                                             @PathVariable("orderSellId") UUID orderSellId) {
        List<Trade> tradeList = tradeRepository.findByOrderBuyIdAndOrderSellId(orderBuyId, orderSellId);
        if (tradeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tradeList.get(0), HttpStatus.OK);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Trade> addTrade(@RequestBody Trade trade) {
        try {
            Trade created = tradeRepository.save(trade);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
