package name.lattuada.trading.controller;

import name.lattuada.trading.model.Security;
import name.lattuada.trading.repository.ISecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/securities")
public class SecurityController {

    @Autowired
    ISecurityRepository securityRepository;

    @GetMapping()
    public ResponseEntity<List<Security>> getSecurities() {
        try {
            List<Security> securityList = securityRepository.findAll();
            if (securityList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(securityList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Security> getSecurityById(@PathVariable("id") UUID uuid) {
        try {
            Optional<Security> optSecurity = securityRepository.findById(uuid);
            return optSecurity.map(security -> new ResponseEntity<>(security, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Security> addSecurity(@Valid @RequestBody Security security) {
        try {
            Security created = securityRepository.save(security);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
