package name.lattuada.trading.tests.unit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import name.lattuada.trading.TradingApplication;
import name.lattuada.trading.model.dto.UserDTO;
import name.lattuada.trading.repository.IUserRepository;

/**
 * 
 * This class is example of unit tests for user controller, 
 * running within context of the Spring Boot application.
 * 
 * This is not comprehensive test solution, but rather just demonstration of technology usage.
 * 
 * @author michal masarik
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TradingApplication.class})
public class UserControllerTest {

	private static final String API_ROOT = "http://localhost:8080/api/users";
	
	@Autowired
	private IUserRepository repository;

	@Before
	public void initRepository() {
		createUserAsUri(createRandomUser());
	}
	
	@After
	public void clearRepository() {
		repository.deleteAll();
		assertThat(repository.count()).isEqualTo(0);
	}

	@Test
	public void whenGetAllUsers_thenOK() {
		final Response response = RestAssured.get(API_ROOT);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}

	@Test
	public void whenGetCreatedUserById_thenOK() {
		final UserDTO user = createRandomUser();
		final String location = createUserAsUri(user);
		final Response response = RestAssured.get(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals(user.getUsername(), response.jsonPath().get("username"));
	}
	
	@Test
	public void whenGetNotExistUserById_thenNotFound() {
	    Response response = RestAssured.get(API_ROOT + "/" + UUID.randomUUID()); 
	    System.out.println(response.getStatusCode());
	    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}
	
	@Test
	public void whenCreateNewUser_thenCreated() {
		UserDTO user = createRandomUser();
	    Response response = RestAssured.given()
	      .contentType(MediaType.APPLICATION_JSON_VALUE)
	      .body(user)
	      .post(API_ROOT);
	    
	    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	}

	@Test
	public void whenInvalidUser_thenError() {
		UserDTO user = createRandomUser();
	    user.setUsername(null);
	    Response response = RestAssured.given()
	      .contentType(MediaType.APPLICATION_JSON_VALUE)
	      .body(user)
	      .post(API_ROOT);
	    
	    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}
	
	private UserDTO createRandomUser() {
		UserDTO user = new UserDTO();
		user.setUsername(randomAlphabetic(10));
		user.setPassword(randomAlphabetic(10));
		return user;
	}

	private String createUserAsUri(UserDTO user) {
		Response response = RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(user).post(API_ROOT);
		return API_ROOT + "/" + response.jsonPath().get("id");
	}
}
