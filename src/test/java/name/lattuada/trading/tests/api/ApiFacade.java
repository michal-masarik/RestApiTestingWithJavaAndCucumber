package name.lattuada.trading.tests.api;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.TradeDTO;
import name.lattuada.trading.model.dto.UserDTO;
import name.lattuada.trading.tests.CucumberTest;

/**
 * This class provides all possible REST API calls to trading application.
 * This serves as single end point interface for tests when communicating with server.
 * Solution is based on Facade design pattern. 
 * 
 * @author michal masarik
 *
 */
public class ApiFacade {

	private final RestUtility restUtility = new RestUtility();
	private static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);

	/**
	 * method returns list of all users stored on server
	 * @return list of all users
	 */
	public List<UserDTO> getAllUsers() {
		ObjectMapper mapper = new ObjectMapper();
		List<UserDTO> userList = mapper.convertValue(restUtility.get("api/users/", List.class),
				new TypeReference<List<UserDTO>>() {
				});
		return userList;
	}

	/**
	 * method creates user and returns the same user in response
	 * @param user 
	 * @return created user
	 */
	public UserDTO createUser(UserDTO user) {
		UserDTO returnedUser = restUtility.post("api/users", user, UserDTO.class);
		logger.info("User created: {}", returnedUser);
		return returnedUser;
	}

	/**
	 * method returns user based on user's ID
	 * @param uuid 
	 * @return user
	 */
	public UserDTO getUser(UUID uuid) {
		UserDTO returnedUser = restUtility.get("api/users/" + uuid.toString(), UserDTO.class);
		return returnedUser;
	}

	/**
	 * method returns list of all securities
	 * @return list of all securities
	 */
	public List<SecurityDTO> getAllSecurities() {
		ObjectMapper mapper = new ObjectMapper();
		List<SecurityDTO> securityList = mapper.convertValue(restUtility.get("api/securities/", List.class),
				new TypeReference<List<SecurityDTO>>() {
				});
		return securityList;
	}

	/**
	 * method creates new security and returns it in response
	 * @param security
	 * @return created security
	 */
	public SecurityDTO createSecurity(SecurityDTO security) {
		SecurityDTO returnedSecurity = restUtility.post("api/securities", security, SecurityDTO.class);
		logger.info("Security created: {}", returnedSecurity);
		return returnedSecurity;
	}

	/**
	 * method returns security based on security id
	 * @param uuid
	 * @return security
	 */
	public SecurityDTO getSecurity(UUID uuid) {
		SecurityDTO returnedSecurity = restUtility.get("api/securities/" + uuid.toString(), SecurityDTO.class);
		return returnedSecurity;
	}

	/**
	 * method returns list of all existing orders
	 * @return list of existing orders
	 */
	public List<OrderDTO> getAllOrders() {
		ObjectMapper mapper = new ObjectMapper();
		List<OrderDTO> ordersList = mapper.convertValue(restUtility.get("api/orders/", List.class),
				new TypeReference<List<OrderDTO>>() {
				});
		return ordersList;
	}

	/**
	 * method creates new order and returns it in response
	 * @param order
	 * @return created order
	 */
	public OrderDTO createOrder(OrderDTO order) {
		OrderDTO returnedOrder = restUtility.post("api/orders", order, OrderDTO.class);
		logger.info("Order created: {}", returnedOrder);
		return returnedOrder;
	}

	/**
	 * method returns order based on order id
	 * @param uuid
	 * @return order
	 */
	public OrderDTO getOrder(UUID uuid) {
		OrderDTO returnedOrder = restUtility.get("api/orders/" + uuid.toString(), OrderDTO.class);
		return returnedOrder;
	}

	/**
	 * method returns list of all trades
	 * @return list of trades
	 */
	public List<TradeDTO> getAllTrades() {
		ObjectMapper mapper = new ObjectMapper();
		List<TradeDTO> tradesList = mapper.convertValue(restUtility.get("api/trades/", List.class),
				new TypeReference<List<TradeDTO>>() {
				});
		return tradesList;
	}

	/**
	 * method returns trade based on its id
	 * @param uuid
	 * @return trade
	 */
	public TradeDTO getTrade(UUID uuid) {
		TradeDTO returnedTrade = restUtility.get("api/trades/" + uuid.toString(), TradeDTO.class);
		return returnedTrade;
	}

	/**
	 * method returns trade based on the buy and sell order identifiers
	 * @param orderBuyId order buy id
	 * @param orderSellId order sell id
	 * @return trade
	 */
	public TradeDTO getTrade(UUID orderBuyId, UUID orderSellId) {
		TradeDTO returnedTrade = restUtility.get(
				"api/trades/orderBuyId/" + orderBuyId.toString() + "/orderSellId/" + orderSellId.toString(),
				TradeDTO.class);
		return returnedTrade;
	}
}
