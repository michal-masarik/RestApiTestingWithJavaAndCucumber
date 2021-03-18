package name.lattuada.trading.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import name.lattuada.trading.model.User;
import name.lattuada.trading.repository.IUserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/users")
@Api()
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    IUserRepository userRepository;

    @GetMapping()
    @ApiOperation(value = "Get list of users",
            notes = "Returns a list of users")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No users"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<List<User>> getUsers() {
        try {
            List<User> userList = userRepository.findAll();
            if (userList.isEmpty()) {
                logger.info("No users found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.debug("Found {} users: {}", userList.size(), userList);
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception caught", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get a specific user",
            notes = "Returns specific user given his id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No users found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<User> getUserById(@PathVariable("id") UUID uuid) {
        try {
            Optional<User> optUser = userRepository.findById(uuid);
            return optUser.map(user -> {
                logger.debug("User found: {}", user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }).orElseGet(() -> {
                logger.warn("No user found having id {}", uuid);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            });
        } catch (Exception e) {
            logger.error("Exception caught", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create user",
            notes = "Create a new user. Note: his ID is not mandatory, but it will be automatically generated. "
                    + "His password will be hashed with SHA-256")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        try {
            // Hash password with SHA-256
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
            User created = userRepository.save(user);
            logger.info("Added user {}", created);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Exception caught", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
