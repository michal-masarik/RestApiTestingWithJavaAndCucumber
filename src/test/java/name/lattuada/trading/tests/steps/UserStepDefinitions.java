package name.lattuada.trading.tests.steps;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.client.HttpClientErrorException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.model.dto.UserDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods connected with User endpoint.
 * This methods are typically processed by {@link UserController} and available on /api/users for REST API
 * 
 * @author michal masarik
 *
 */
public class UserStepDefinitions implements IStepDefinitions {

	private static int numberOfUsers = 0;
	private static UUID nonExistingUserId = null;
	
	@When("user {string} is created")
	public void user_is_created(String username) {
		logger.trace("username = \"{}\"", username);
		createUser(username);
	}

	@Then("user {string} exists")
	public void user_exists(String username) {
		logger.trace("username = \"{}\"", username);
		UserDTO user = getUser(username);
		assertEquals(username, user.getUsername());
	}

	@Then("both users {string} and {string} exist")
	public void both_users_and_exist(String firstUsername, String secondUsername) {
		logger.trace("firstUsername = \"{}\"; secondUsername = \"{}\"", firstUsername, secondUsername);
		List<UserDTO> userList = api.getAllUsers();
		assertUserNotInList(firstUsername, userList);
		assertUserNotInList(secondUsername, userList);
	}

	@Given("known number of users")
	public void known_number_of_users() {
		numberOfUsers = getNumberOfUsers();
	}

	@Then("only {int} user was added")
	public void only_user_was_added(Integer numberOfAddedUsers) {
		int updatedNumberOfUsers = getNumberOfUsers();
		assertEquals("Unexpected number of users", numberOfUsers + numberOfAddedUsers, updatedNumberOfUsers);	
	}
	
	@When("\\/api\\/users\\/id is asked for non-existing user id")
	public void api_users_id_is_asked_for_non_existing_user_id() {
	   nonExistingUserId = UUID.randomUUID();
	}
	@Then("server returns user not found message")
	public void server_returns_user_not_found_message() {
		assertThatThrownBy(() -> api.getUser(nonExistingUserId))
		.isInstanceOf(HttpClientErrorException.NotFound.class);
	}
	   
	private int getNumberOfUsers() {
		List<UserDTO> userList = api.getAllUsers();
		return userList.size();
	}

	private void assertUserNotInList(String firstUsername, List<UserDTO> userList) {
		UUID firstUserExpectedId = context.userMap.get(firstUsername).getId();
		UserDTO firstUser = userList.stream().filter(user -> firstUserExpectedId.equals(user.getId())).findAny()
				.orElse(null);
		assertNotNull("User " + firstUsername + " doesn't exist", firstUser);
	}
	
	private UserDTO getUser(String username) {
		UUID userId = context.userMap.get(username).getId();
		return api.getUser(userId);
	}
	
	private void createUser(String username) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		userDTO.setPassword(RandomStringUtils.randomAlphanumeric(64));
		UserDTO returnedUser = api.createUser(userDTO);
		context.userMap.put(username, returnedUser);	
	}	
}
