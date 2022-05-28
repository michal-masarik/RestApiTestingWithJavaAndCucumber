package name.lattuada.trading.tests.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import name.lattuada.trading.controller.SecurityController;
import name.lattuada.trading.model.dto.SecurityDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods
 * connected with Security endpoint. This methods are typically processed by
 * {@link SecurityController} and available on /api/securities for REST API
 * 
 * @author michal masarik
 *
 */
public class SecurityStepDefinitions implements IStepDefinitions {

	private static int numberOfSecurities = 0;
	private static SecurityDTO randomSecurity;
	private static String securityName;

	@When("security {string} is created")
	@Given("one security {string} is created")
	public void one_security_is_created(String securityName) {
		SecurityDTO securityDTO = new SecurityDTO();
		securityDTO.setName(securityName);
		SecurityDTO returnedSecurity = api.createSecurity(securityDTO);
		context.securityMap.put(securityName, returnedSecurity);
	}

	@Given("known number of securities")
	public void known_number_of_securities() {
		numberOfSecurities = getNumberOfExistingSecurities();
	}

	@Then("only {int} security was added")
	public void only_security_was_added(Integer numberOfAddedSecurities) {
		int updatedNumberOfSecurities = getNumberOfExistingSecurities();
		assertEquals("Unexpected number of securities", numberOfSecurities + numberOfAddedSecurities,
				updatedNumberOfSecurities);
	}

	@Then("security {string} exists")
	public void security_exists(String securityName) {
		logger.trace("securityName = \"{}\"", securityName);
		SecurityDTO security = getSecurity(securityName);
		assertEquals(securityName, security.getName());
	}

	@Then("both securities {string} and {string} exist")
	public void both_securities_and_exist(String firstSecurityname, String secondSecurityname) {
		logger.trace("firstSecurityname = \"{}\"; secondSecurityname = \"{}\"", firstSecurityname, secondSecurityname);
		List<SecurityDTO> securityList = api.getAllSecurities();
		assertSecurityNotInList(firstSecurityname, securityList);
		assertSecurityNotInList(secondSecurityname, securityList);
	}

	@Given("a random non-existing security")
	public void a_random_non_existing_security() {
		randomSecurity = new SecurityDTO();
		randomSecurity.setId(UUID.randomUUID());
		randomSecurity.setName(RandomStringUtils.randomAlphabetic(5));
	}

	@When("we ask for the random security via the {string}")
	public void we_ask_for_the_random_security_via_the(String location) {
		context.response = RestAssured.get(location + "/" + randomSecurity.getId());
	}

	@When("we create new security via the {string} succesfully")
	public void we_create_new_security_via_the_succesfully(String location) {
		SecurityDTO security = new SecurityDTO();
		security.setName(RandomStringUtils.randomAlphabetic(5));
		securityName = security.getName();
		context.response = given().contentType(ContentType.JSON).body(security).post(location);
	}

	@Then("security is returned in body of response")
	public void security_is_returned_in_body_of_response() {
		context.response.then().body("name", equalTo(securityName));
	}

	@When("we create security via the {string} without name")
	public void we_create_security_via_the_without_name(String location) {
		SecurityDTO security = new SecurityDTO();
		context.response = given().contentType(ContentType.JSON).body(security).post(location);
	}
	
	public static SecurityDTO createRandomSecurity() {
		SecurityDTO security = new SecurityDTO();
		security.setId(UUID.randomUUID());
		security.setName(RandomStringUtils.randomAlphabetic(5));
		return security;
	}

	private void assertSecurityNotInList(String securityname, List<SecurityDTO> securityList) {
		UUID securityExpectedId = context.securityMap.get(securityname).getId();
		SecurityDTO security = securityList.stream().filter(s -> securityExpectedId.equals(s.getId())).findAny()
				.orElse(null);
		assertNotNull("Security " + securityname + " doesn't exist", security);
	}

	private SecurityDTO getSecurity(String securityName) {
		assertTrue(String.format("Unknown security \"%s\"", securityName),
				context.securityMap.containsKey(securityName));
		UUID securityId = context.securityMap.get(securityName).getId();
		return api.getSecurity(securityId);
	}

	private int getNumberOfExistingSecurities() {
		List<SecurityDTO> securityList = api.getAllSecurities();
		return securityList.size();
	}
}
