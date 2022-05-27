package name.lattuada.trading.tests.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import name.lattuada.trading.controller.UserController;
import name.lattuada.trading.model.dto.UserDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods
 * connected with User endpoint. This methods are typically processed by
 * {@link UserController} and available on /api/users for REST API
 * 
 * @author michal masarik
 *
 */
public class UserStepDefinitions implements IStepDefinitions {

	private static int numberOfUsers;
	private static UUID nonExistingUserId;
	private static Response response;
	private static String username;

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

	@Given("a random non-existing user")
	public void a_random_non_existing_user() {
		nonExistingUserId = UUID.randomUUID();
	}

	@When("we ask for the random user via the {string}")
	public void we_ask_for_the_random_user_via_the(String location) {
		response = RestAssured.get(location + "/" + nonExistingUserId);
	}

	@When("we create new user via the {string} succesfully")
	public void we_create_new_user_via_the_succesfully(String string) {
		UserDTO user = createNewUser();
		response = given().contentType(ContentType.JSON).body(user).post("/api/users/");
	}
	
	@When("we create new user via the {string} without password")
	public void we_create_new_user_via_the_without_password(String location) {
		UserDTO user = createNewUser();
		user.setPassword(null);
		response = given().contentType(ContentType.JSON).body(user).post(location);
	}

	@Then("server responds with code {int}")
	public void server_responds_with_code(Integer code) {
		response.then().assertThat().statusCode(code);
	}

	@Then("user is returned in body of response")
	public void user_is_returned_in_body_of_response() {
		response.then().body("username", equalTo(username));
	}
	
	private UserDTO createNewUser() {
		UserDTO user = new UserDTO();
		user.setUsername(RandomStringUtils.randomAlphabetic(5));
		user.setPassword(RandomStringUtils.randomAlphanumeric(10));
		username = user.getUsername();
		return user;
	}
}
