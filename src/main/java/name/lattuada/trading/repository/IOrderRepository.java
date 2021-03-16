package name.lattuada.trading.repository;

import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IOrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findBySecurityIdAndTypeAndFulfilled(UUID securityId, EOrderType type, Boolean fulfilled);

}
