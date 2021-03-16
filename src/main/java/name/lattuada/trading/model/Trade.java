package name.lattuada.trading.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Entity
@Table(name = "trades")
@Getter
@Setter
@ToString
public class Trade {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trd_id")
    private UUID id;

    @name.lattuada.trading.validator.UUID
    @Column(name = "trd_ord_sell_id")
    private UUID orderSellId;

    @name.lattuada.trading.validator.UUID
    @Column(name = "trd_ord_buy_id")
    private UUID orderBuyId;

    @NotNull
    @Positive
    @Column(name = "trd_price")
    private Double price;

    @NotNull
    @Positive
    @Column(name = "trd_quantity")
    private Long quantity;

}
