package name.lattuada.trading.model.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import name.lattuada.trading.model.EOrderType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ord_id")
    private UUID id;

    @name.lattuada.trading.validator.UUID
    @Column(name = "ord_usr_id")
    private UUID userId;

    @name.lattuada.trading.validator.UUID
    @Column(name = "ord_sec_id")
    private UUID securityId;

    @NotNull
    @Column(name = "ord_type")
    @Enumerated(EnumType.STRING)
    private EOrderType type;

    @Positive
    @Column(name = "ord_price")
    private Double price;

    @NotNull
    @Positive
    @Column(name = "ord_quantity")
    private Long quantity;

    @Column(name = "ord_fulfilled")
    private Boolean fulfilled = Boolean.FALSE;

}
