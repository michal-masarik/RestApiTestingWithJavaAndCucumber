package name.lattuada.trading.tests.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.controller.OrderController;
import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.dto.OrderDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods connected with Order endpoint.
 * This methods are typically processed by {@link OrderController} and available on /api/orders for REST API
 * 
 * @author michal masarik
 *
 */
public class OrderStepDefinitions implements IStepDefinitions {

	private static int numberOfOrders = 0;

	@When("user {string} puts a {string} order for security {string} with a price of {double} and quantity of {long}")
	@And("user {string} puts a {string} order for security {string} with a price of {double} and a quantity of {long}")
	public void userPutAnOrder(String userName, String orderType, String securityName, Double price, Long quantity) {
		logger.trace(
				"Got username = \"{}\"; orderType = \"{}\"; securityName = \"{}\"; price = \"{}\"; quantity = \"{}\"",
				userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
		assertTrue(String.format("Unknown user \"%s\"", userName), context.userMap.containsKey(userName));
		assertTrue(String.format("Unknown security \"%s\"", securityName),
				context.securityMap.containsKey(securityName));
		createOrder(userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
	}

	@Then("a {string} order from user {string} for security {string} with a price of {double} and quantity of {long} exists")
	public void order_from_user_for_security_with_a_price_of_and_quantity_of_exists(String orderType, String userName,
			String securityName, Double price, Long quantity) {
		OrderDTO expectedOrder;
		if (orderType.equalsIgnoreCase(EOrderType.BUY.toString())) {
			expectedOrder = context.buyOrder;
		} else {
			expectedOrder = context.sellOrder;
		}
		OrderDTO actualOrder = api.getOrder(expectedOrder.getId());
		assertEquals("Unexpected order type.", expectedOrder.getType(), actualOrder.getType());
		assertEquals("Unexpected order price.", expectedOrder.getPrice(), actualOrder.getPrice());
		assertEquals("Unexpected order quantity.", expectedOrder.getQuantity(), actualOrder.getQuantity());
		assertEquals("Unexpected order userID.", expectedOrder.getUserId(), actualOrder.getUserId());
		assertEquals("Unexpected order securityID.", expectedOrder.getSecurityId(), actualOrder.getSecurityId());
	}

	@Given("known number of orders")
	public void known_number_of_orders() {
		numberOfOrders = getNumberOfOrders();
	}

	@Then("only {int} order was added")
	public void only_order_was_added(Integer numberOfAddedOrders) {
		int updatedNumberOfOrders = getNumberOfOrders();
		assertEquals("Unexpected number of Orders", numberOfOrders + numberOfAddedOrders, updatedNumberOfOrders);
	}

	private int getNumberOfOrders() {
		List<OrderDTO> ordersList = api.getAllOrders();
		return ordersList.size();
	}

	private void createOrder(String userName, EOrderType orderType, String securityName, Double price, Long quantity) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setUserId(context.userMap.get(userName).getId());
		orderDTO.setSecurityId(context.securityMap.get(securityName).getId());
		orderDTO.setType(orderType);
		orderDTO.setPrice(price);
		orderDTO.setQuantity(quantity);
		OrderDTO orderReturned = api.createOrder(orderDTO);
		if (EOrderType.BUY.equals(orderType)) {
			context.buyOrder = orderReturned;
			logger.trace("put in context - buyOrder:" + context.buyOrder);
		} else {
			context.sellOrder = orderReturned;
			logger.trace("put in context - sellOrder:" + context.sellOrder);
		}
	}
}
