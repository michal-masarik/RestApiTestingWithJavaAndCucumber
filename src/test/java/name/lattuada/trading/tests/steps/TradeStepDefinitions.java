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
		logger.trace("Got price = \"{}\"; quantity = \"{}\"", price, quantity);
		TradeDTO trade = api.getTrade(context.buyOrder.getId(), context.sellOrder.getId());
		assertEquals("Price not expected", trade.getPrice(), price);
		assertEquals("Quantity not expected", trade.getQuantity(), quantity);
	}

	@Then("no trades occur")
	public void noTradesOccur() {
		assertThatThrownBy(() -> api.getTrade(context.buyOrder.getId(), context.sellOrder.getId()))
				.isInstanceOf(HttpClientErrorException.NotFound.class);
	}

	@Then("a trade occurs between {string} and {string}")
	public void a_trade_occurs_between_and(String buyerName, String sellerName) {
		assertNotNull(getTradeBetweenTwoUsers(buyerName, sellerName));
	}

	@Then("a trade occurs between {string} and {string} with the price of {double} and quantity of {long}")
	public void a_trade_occurs_between_and_with_the_price_of_and_quantity_of(String buyerName, String sellerName,
			Double price, Long quantity) {

		TradeDTO actualTrade = getTradeBetweenTwoUsers(buyerName, sellerName);
		assertNotNull(actualTrade);
		assertEquals("Unexpected price in trade " + actualTrade.getId() + " ", price, actualTrade.getPrice());
		assertEquals("Unexpected quantity in trade " + actualTrade.getId() + " ", quantity, actualTrade.getQuantity());
	}

	private TradeDTO getTradeBetweenTwoUsers(String buyerName, String sellerName) {
		UUID expectedBuyerId = context.userMap.get(buyerName).getId();
		UUID expectedSellerId = context.userMap.get(sellerName).getId();
		UUID expectedBuyOrderId = null;
		UUID expectedSellOrderId = null;

		List<OrderDTO> orderList = api.getAllOrders();
		for (OrderDTO orderDTO : orderList) {
			if (orderDTO.getUserId().equals(expectedBuyerId)) {
				expectedBuyOrderId = orderDTO.getId();
			} else if (orderDTO.getUserId().equals(expectedSellerId)) {
				expectedSellOrderId = orderDTO.getId();
			}
		}
		return api.getTrade(expectedBuyOrderId, expectedSellOrderId);
	}
}
