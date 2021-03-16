package name.lattuada.trading.repository;

import name.lattuada.trading.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ITradeRepository extends JpaRepository<Trade, UUID> {

    List<Trade> findByOrderBuyIdAndOrderSellId(UUID orderBuyId, UUID orderSellId);

}
