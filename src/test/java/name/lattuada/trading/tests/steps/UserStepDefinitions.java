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
	private static String username;

	@When("user {string} is created")
	public void createNewUser(String username) {
		LOGGER.trace("username = \"{}\"", username);
		UserDTO createdUser = createUser(username);
		assertNotNull(createdUser);
	}

	@Then("user {string} exists")
	public void verifyThatUserExists(String username) {
		LOGGER.trace("username = \"{}\"", username);
		UserDTO user = getUser(username);
		assertEquals(username, user.getUsername());
	}

	@Then("both users {string} and {string} exist")
	public void verifyThatBothUsersExist(String firstUsername, String secondUsername) {
		LOGGER.trace("firstUsername = \"{}\"; secondUsername = \"{}\"", firstUsername, secondUsername);
		List<UserDTO> userList = API.getAllUsers();
		assertUserIsInList(firstUsername, userList);
		assertUserIsInList(secondUsername, userList);
	}

	@Given("known number of users")
	public void getCurrentNumberOfUsers() {
		numberOfUsers = getNumberOfUsers();
	}

	@Then("only {int} user was added")
	public void verifyNumberOfUsersAdded(Integer numberOfAddedUsers) {
		int updatedNumberOfUsers = getNumberOfUsers();
		assertEquals("Unexpected number of users", numberOfUsers + numberOfAddedUsers, updatedNumberOfUsers);
	}

	@Given("a random non-existing user")
	public void createRandomNonExistingUser() {
		nonExistingUserId = UUID.randomUUID();
	}

	@When("we ask for the random user via the {string}")
	public void getRandomUser(String location) {
		CONTEXT.response = RestAssured.get(location + "/" + nonExistingUserId);
	}

	@When("we create new user via the {string} succesfully")
	public void createNewUserSuccesfully(String location) {
		UserDTO user = createUserEntity();
		CONTEXT.response = given().contentType(ContentType.JSON).body(user).post(location);
	}

	@When("we create new user via the {string} without password")
	public void createUserWithoutPassword(String location) {
		UserDTO user = createUserEntity();
		user.setPassword(null);
		CONTEXT.response = given().contentType(ContentType.JSON).body(user).post(location);
	}

	@Then("user is returned in body of response")
	public void verifyThatUserIsReturnedInResponseBody() {
		CONTEXT.response.then().body("username", equalTo(username));
	}

	public static UserDTO createUserEntity() {
		UserDTO user = new UserDTO();
		user.setId(UUID.randomUUID());
		user.setUsername(RandomStringUtils.randomAlphabetic(5));
		user.setPassword(RandomStringUtils.randomAlphanumeric(10));
		username = user.getUsername();
		return user;
	}

	private UserDTO createUser(String username) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		userDTO.setPassword(RandomStringUtils.randomAlphanumeric(64));
		UserDTO returnedUser = API.createUser(userDTO);
		CONTEXT.userMap.put(username, returnedUser);
		return returnedUser;
	}

	private int getNumberOfUsers() {
		List<UserDTO> userList = API.getAllUsers();
		return userList.size();
	}

	private void assertUserIsInList(String username, List<UserDTO> userList) {
		UUID userExpectedId = CONTEXT.userMap.get(username).getId();
		UserDTO user = userList.stream().filter(u -> userExpectedId.equals(u.getId())).findAny().orElse(null);
		assertNotNull("User " + username + " doesn't exist", user);
	}

	private UserDTO getUser(String username) {
		UUID userId = CONTEXT.userMap.get(username).getId();
		return API.getUser(userId);
	}
}
