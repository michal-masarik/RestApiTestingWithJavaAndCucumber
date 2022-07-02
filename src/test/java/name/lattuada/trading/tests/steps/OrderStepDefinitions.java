package name.lattuada.trading.tests.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import name.lattuada.trading.controller.OrderController;
import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.UserDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods
 * connected with Order endpoint. This methods are typically processed by
 * {@link OrderController} and available on /api/orders for REST API
 * 
 * @author michal masarik
 *
 */
public class OrderStepDefinitions implements IStepDefinitions {

	private static int numberOfOrders = 0;
	private static UUID nonExistingOrderId;
	private static SecurityDTO randomSecurity;
	private static UserDTO randomUser;

	@When("user {string} puts a {string} order for security {string} with a price of {double} and quantity of {long}")
	@And("user {string} puts a {string} order for security {string} with a price of {double} and a quantity of {long}")
	public void createNewOrder(String userName, String orderType, String securityName, Double price, Long quantity) {
		LOGGER.trace(
				"Got username = \"{}\"; orderType = \"{}\"; securityName = \"{}\"; price = \"{}\"; quantity = \"{}\"",
				userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
		assertTrue(String.format("Unknown user \"%s\"", userName), CONTEXT.userMap.containsKey(userName));
		assertTrue(String.format("Unknown security \"%s\"", securityName),
				CONTEXT.securityMap.containsKey(securityName));
		createOrder(userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
	}

	@Then("a {string} order from user {string} for security {string} with a price of {double} and quantity of {long} exists")
	public void orderWithUserWithTypeWithSecurityWithPriceWithQuantityExists(String orderType, String userName,
			String securityName, Double price, Long quantity) {
		OrderDTO expectedOrder;
		if (orderType.equals(EOrderType.BUY.toString())) {
			expectedOrder = CONTEXT.buyOrder;
		} else {
			expectedOrder = CONTEXT.sellOrder;
		}
		OrderDTO actualOrder = API.getOrder(expectedOrder.getId());
		assertEquals("Unexpected order type.", expectedOrder.getType(), actualOrder.getType());
		assertEquals("Unexpected order price.", expectedOrder.getPrice(), actualOrder.getPrice());
		assertEquals("Unexpected order quantity.", expectedOrder.getQuantity(), actualOrder.getQuantity());
		assertEquals("Unexpected order userID.", expectedOrder.getUserId(), actualOrder.getUserId());
		assertEquals("Unexpected order securityID.", expectedOrder.getSecurityId(), actualOrder.getSecurityId());
	}

	@Given("known number of orders")
	public void getCurrentNumberOfOrders() {
		numberOfOrders = getNumberOfOrders();
	}

	@Then("only {int} order was added")
	public void verifyNumberNewOrdersAdded(Integer numberOfAddedOrders) {
		int updatedNumberOfOrders = getNumberOfOrders();
		assertEquals("Unexpected number of Orders", numberOfOrders + numberOfAddedOrders, updatedNumberOfOrders);
	}

	@Given("a random non-existing order")
	public void createNotExistingOrderId() {
		nonExistingOrderId = UUID.randomUUID();
	}

	@When("we ask for the random order via the {string}")
	public void getNotExistingOrder(String location) {
		CONTEXT.response = RestAssured.get(location + "/" + nonExistingOrderId);
	}

	@Given("random user and a random security")
	public void createRandomOrderAndRandomSecurity() {
		randomSecurity = SecurityStepDefinitions.createRandomSecurity();
		randomUser = UserStepDefinitions.createUserEntity();
	}

	@When("we create new order via the {string}")
	@When("we create new order via the {string} succesfully")
	public void createOrder(String location) {
		OrderDTO order = new OrderDTO();
		order.setUserId(randomUser.getId());
		order.setSecurityId(randomSecurity.getId());
		order.setType(EOrderType.BUY);
		order.setPrice(Double.valueOf(RandomStringUtils.randomNumeric(3)));
		order.setQuantity(Long.valueOf(RandomStringUtils.randomNumeric(3)));
		CONTEXT.response = given().contentType(ContentType.JSON).body(order).post(location);
	}

	@When("we create invalid order via the {string} without quantity and price")
	public void createInvalidOrder(String location) {
		OrderDTO order = new OrderDTO();
		order.setUserId(randomUser.getId());
		order.setSecurityId(randomSecurity.getId());
		order.setType(EOrderType.BUY);
		CONTEXT.response = given().contentType(ContentType.JSON).body(order).post(location);
	}

	@Then("order is returned in body of response")
	public void verifyOrderIsReturnedInResponse() {
		Response response = CONTEXT.response;
		response.then().body("userId", equalTo(randomUser.getId().toString()));
		response.then().body("securityId", equalTo(randomSecurity.getId().toString()));
	}

	public static OrderDTO createOrderEntity(String userName, EOrderType orderType, String securityName, Double price,
			Long quantity) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setUserId(CONTEXT.userMap.get(userName).getId());
		orderDTO.setSecurityId(CONTEXT.securityMap.get(securityName).getId());
		orderDTO.setType(orderType);
		orderDTO.setPrice(price);
		orderDTO.setQuantity(quantity);
		return orderDTO;
	}

	private int getNumberOfOrders() {
		List<OrderDTO> ordersList = API.getAllOrders();
		return ordersList.size();
	}

	private void createOrder(String userName, EOrderType orderType, String securityName, Double price, Long quantity) {
		OrderDTO orderDTO = createOrderEntity(userName, orderType, securityName, price, quantity);
		OrderDTO orderReturned = API.createOrder(orderDTO);
		if (EOrderType.BUY == (orderType)) {
			CONTEXT.buyOrder = orderReturned;
		} else {
			CONTEXT.sellOrder = orderReturned;
		}
	}
}
