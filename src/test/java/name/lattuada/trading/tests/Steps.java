package name.lattuada.trading.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.TradeDTO;
import name.lattuada.trading.model.dto.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Steps extends RestUtility {

    private static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);
    private final Map<String, SecurityDTO> securityMap;
    private final Map<String, UserDTO> userMap;
    private OrderDTO buyOrder;
    private OrderDTO sellOrder;

    Steps() {
        securityMap = new HashMap<>();
        userMap = new HashMap<>();
    }

    @Given("one security {string} and two users {string} and {string} exist")
    public void oneSecurityAndTwoUsers(String securityName, String userName1, String userName2) throws JsonProcessingException {
        logger.trace("Got securityName = \"{}\"; userName1 = \"{}\"; userName2 = \"{}\"",
                securityName, userName1, userName2);
        createUser(userName1);
        createUser(userName2);
        createSecurity(securityName);
    }

    @When("user {string} puts a {string} order for security {string} with a price of {double} and quantity of {long}")
    @And("user {string} puts a {string} order for security {string} with a price of {double} and a quantity of {long}")
    public void userPutAnOrder(String userName, String orderType, String securityName, Double price, Long quantity) throws JsonProcessingException {
        logger.trace("Got username = \"{}\"; orderType = \"{}\"; securityName = \"{}\"; price = \"{}\"; quantity = \"{}\"",
                userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
        assertTrue(String.format("Unknown user \"%s\"", userName),
                userMap.containsKey(userName));
        assertTrue(String.format("Unknown security \"%s\"", securityName),
                securityMap.containsKey(securityName));
        createOrder(userName,
                EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)),
                securityName,
                price,
                quantity);
    }

    @Then("a trade occurs with the price of {double} and quantity of {long}")
    public void aTradeOccursWithThePriceOfAndQuantityOf(Double price, Long quantity) {
        logger.trace("Got price = \"{}\"; quantity = \"{}\"",
                price, quantity);
        TradeDTO trade = get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class);
        assertEquals("Price not expected", trade.getPrice(), price);
        assertEquals("Quantity not expected", trade.getQuantity(), quantity);
    }

    private void createUser(String userName1) throws JsonProcessingException {
        UserDTO user1 = new UserDTO();
        user1.setUsername(userName1);
        user1.setPassword(RandomStringUtils.randomAlphanumeric(64));
        UserDTO user1Returned = post("api/users",
                new ObjectMapper().writer().writeValueAsString(user1),
                UserDTO.class);
        userMap.put(userName1, user1Returned);
        logger.info("User created: {}", user1Returned);
    }

    private void createSecurity(String securityName) throws JsonProcessingException {
        SecurityDTO security = new SecurityDTO();
        security.setName(securityName);
        SecurityDTO securityReturned = post("api/securities",
                new ObjectMapper().writer().writeValueAsString(security),
                SecurityDTO.class);
        securityMap.put(securityName, securityReturned);
        logger.info("Security created: {}", securityReturned);
    }

    private void createOrder(String userName,
                             EOrderType orderType,
                             String securityName,
                             Double price,
                             Long quantity) throws JsonProcessingException {
        OrderDTO order = new OrderDTO();
        order.setUserId(userMap.get(userName).getId());
        order.setSecurityId(securityMap.get(securityName).getId());
        order.setType(orderType);
        order.setPrice(price);
        order.setQuantity(quantity);
        OrderDTO orderReturned = post("api/orders",
                new ObjectMapper().writer().writeValueAsString(order),
                OrderDTO.class);
        if (EOrderType.BUY.equals(orderType)) {
            buyOrder = orderReturned;
        } else {
            sellOrder = orderReturned;
        }
        logger.info("Order created: {}", orderReturned);
    }

}
