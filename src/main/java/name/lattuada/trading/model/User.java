package name.lattuada.trading.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    @JsonInclude(Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usr_id")
    private UUID id;

    @NotBlank
    @Column(name = "usr_username")
    private String username;

    @NotBlank
    @Column(name = "usr_password")
    @ToString.Exclude
    private String password;

}
