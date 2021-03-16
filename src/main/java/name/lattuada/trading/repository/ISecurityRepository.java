package name.lattuada.trading.repository;

import name.lattuada.trading.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ISecurityRepository extends JpaRepository<Security, UUID> {
}
