package name.lattuada.trading.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "securities")
@Getter
@Setter
@ToString
public class Security {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sec_id")
    private UUID id;

    @NotBlank
    @Column(name = "sec_name")
    private String name;

}
