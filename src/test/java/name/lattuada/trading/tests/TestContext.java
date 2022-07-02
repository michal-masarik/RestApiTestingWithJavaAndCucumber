package name.lattuada.trading.tests;

import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.UserDTO;
import name.lattuada.trading.tests.steps.TradeStepDefinitions;
import name.lattuada.trading.tests.steps.UserStepDefinitions;

/**
 * This singleton class serves as shared cache for persisting 
 * any test elements between different step definition classes. 
 * When it's possible testing elements are saved as property 
 * of single step definition class, but in many cases happens 
 * that test context is switched to different step definition class.
 * 
 * Typical example of usage can be:
 * 
 * {@link UserStepDefinitions#user_is_created(String)} creates user and 
 * also saves this user in local context for later usage as expected value in test
 * 
 * Later {@link TradeStepDefinitions#aTradeOccursWithThePriceOfAndQuantityOf(Double, Long)}
 * need user id to be used as expected value for its verification
 * 
 * Content of this class can be cleared when needed. 
 * Current strategy clears content before each test class. 
 * Not clearing can cause issues when using repeated test data across many tests.
 * 
 * @author michal masarik
 *
 */
public class TestContext {

	private static TestContext singleton = null;

	public final Map<String, UserDTO> userMap;
	public final Map<String, SecurityDTO> securityMap;
	public final Map<String, SecurityDTO> orderMap;
	public OrderDTO buyOrder;
	public OrderDTO sellOrder;
	public Response response;

	private TestContext() {
		securityMap = new HashMap<>();
		userMap = new HashMap<>();
		orderMap = new HashMap<>();
		buyOrder = new OrderDTO();
		sellOrder = new OrderDTO();
		response = null;
	}

	public static TestContext getInstance() {
		if (singleton == null) {
			singleton = new TestContext();
		}
		return singleton;
	}

	public void clean() {
		securityMap.clear();
		userMap.clear();
		orderMap.clear();
		buyOrder = null;
		sellOrder = null;
		response = null;
	}
}
