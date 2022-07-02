package name.lattuada.trading.tests.steps;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;
import org.springframework.web.client.HttpClientErrorException;
import io.cucumber.java.en.Then;
import name.lattuada.trading.controller.TradeController;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.TradeDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods
 * connected with Trade endpoint. This methods are typically processed by
 * {@link TradeController} and available on /api/trades REST API
 * 
 * @author michal masarik
 *
 */
public class TradeStepDefinitions implements IStepDefinitions {

	@Then("a trade occurs with the price of {double} and quantity of {long}")
	public void aTradeOccursWithThePriceOfAndQuantityOf(Double price, Long quantity) {
		LOGGER.trace("Got price = \"{}\"; quantity = \"{}\"", price, quantity);
		TradeDTO trade = API.getTrade(CONTEXT.buyOrder.getId(), CONTEXT.sellOrder.getId());
		assertEquals("Price not expected", trade.getPrice(), price);
		assertEquals("Quantity not expected", trade.getQuantity(), quantity);
	}

	@Then("no trades occur")
	public void noTradesOccur() {
		assertThatThrownBy(() -> API.getTrade(CONTEXT.buyOrder.getId(), CONTEXT.sellOrder.getId()))
				.isInstanceOf(HttpClientErrorException.NotFound.class);
	}

	@Then("a trade occurs between {string} and {string}")
	public void verifyTradeExistsBetweenBuyerAndSeller(String buyerName, String sellerName) {
		assertNotNull(getTradeBetweenTwoUsers(buyerName, sellerName));
	}

	@Then("a trade occurs between {string} and {string} with the price of {double} and quantity of {long}")
	public void verifyTradeExistsBetweenBuyerAndSellerWithPriceAndQuantity(String buyerName, String sellerName,
			Double price, Long quantity) {

		TradeDTO actualTrade = getTradeBetweenTwoUsers(buyerName, sellerName);
		assertNotNull(actualTrade);
		assertEquals("Unexpected price in trade " + actualTrade.getId() + " ", price, actualTrade.getPrice());
		assertEquals("Unexpected quantity in trade " + actualTrade.getId() + " ", quantity, actualTrade.getQuantity());
	}

	private TradeDTO getTradeBetweenTwoUsers(String buyerName, String sellerName) {
		UUID expectedBuyerId = CONTEXT.userMap.get(buyerName).getId();
		UUID expectedSellerId = CONTEXT.userMap.get(sellerName).getId();
		UUID expectedBuyOrderId = null;
		UUID expectedSellOrderId = null;

		List<OrderDTO> orderList = API.getAllOrders();
		for (OrderDTO orderDTO : orderList) {
			if (orderDTO.getUserId().equals(expectedBuyerId)) {
				expectedBuyOrderId = orderDTO.getId();
			} else if (orderDTO.getUserId().equals(expectedSellerId)) {
				expectedSellOrderId = orderDTO.getId();
			}
		}
		return API.getTrade(expectedBuyOrderId, expectedSellOrderId);
	}
}
