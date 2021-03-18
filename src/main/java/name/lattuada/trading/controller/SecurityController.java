package name.lattuada.trading.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "Get list of securities",
            notes = "Returns a list of securities")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No securities"),
            @ApiResponse(code = 500, message = "Server error")
    })
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
    @ApiOperation(value = "Get a specific security",
            notes = "Returns specific security given its id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No securities found"),
            @ApiResponse(code = 500, message = "Server error")
    })
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
    @ApiOperation(value = "Create security",
            notes = "Create a new security. Note: its ID is not mandatory, but it will be automatically generated")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Security created"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<Security> addSecurity(@Valid @RequestBody Security security) {
        try {
            Security created = securityRepository.save(security);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
