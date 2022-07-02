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
	public void createNewSecurity(String securityName) {
		SecurityDTO securityDTO = new SecurityDTO();
		securityDTO.setName(securityName);
		SecurityDTO returnedSecurity = API.createSecurity(securityDTO);
		CONTEXT.securityMap.put(securityName, returnedSecurity);
	}

	@Given("known number of securities")
	public void getNumberOfSecurities() {
		numberOfSecurities = getNumberOfExistingSecurities();
	}

	@Then("only {int} security was added")
	public void verifyNumberOfNewSecurities(Integer numberOfAddedSecurities) {
		int updatedNumberOfSecurities = getNumberOfExistingSecurities();
		assertEquals("Unexpected number of securities", numberOfSecurities + numberOfAddedSecurities,
				updatedNumberOfSecurities);
	}

	@Then("security {string} exists")
	public void verifyThatSecurityExists(String securityName) {
		LOGGER.trace("securityName = \"{}\"", securityName);
		SecurityDTO security = getSecurity(securityName);
		assertEquals(securityName, security.getName());
	}

	@Then("both securities {string} and {string} exist")
	public void verifyThatBothSecuritiesExist(String firstSecurityname, String secondSecurityname) {
		LOGGER.trace("firstSecurityname = \"{}\"; secondSecurityname = \"{}\"", firstSecurityname, secondSecurityname);
		List<SecurityDTO> securityList = API.getAllSecurities();
		assertSecurityIsInList(firstSecurityname, securityList);
		assertSecurityIsInList(secondSecurityname, securityList);
	}

	@Given("a random non-existing security")
	public void createOneRandomSecurity() {
		randomSecurity = new SecurityDTO();
		randomSecurity.setId(UUID.randomUUID());
		randomSecurity.setName(RandomStringUtils.randomAlphabetic(5));
	}

	@When("we ask for the random security via the {string}")
	public void returnRandomSecurity(String location) {
		CONTEXT.response = RestAssured.get(location + "/" + randomSecurity.getId());
	}

	@When("we create new security via the {string} succesfully")
	public void createNewSecuritySuccessfully(String location) {
		SecurityDTO security = new SecurityDTO();
		security.setName(RandomStringUtils.randomAlphabetic(5));
		securityName = security.getName();
		CONTEXT.response = given().contentType(ContentType.JSON).body(security).post(location);
	}

	@Then("security is returned in body of response")
	public void verifySecurityIsReturnedInResponseBody() {
		CONTEXT.response.then().body("name", equalTo(securityName));
	}

	@When("we create security via the {string} without name")
	public void createSecurityWithoutName(String location) {
		SecurityDTO security = new SecurityDTO();
		CONTEXT.response = given().contentType(ContentType.JSON).body(security).post(location);
	}

	public static SecurityDTO createRandomSecurity() {
		SecurityDTO security = new SecurityDTO();
		security.setId(UUID.randomUUID());
		security.setName(RandomStringUtils.randomAlphabetic(5));
		return security;
	}

	private void assertSecurityIsInList(String securityname, List<SecurityDTO> securityList) {
		UUID securityExpectedId = CONTEXT.securityMap.get(securityname).getId();
		SecurityDTO security = securityList.stream().filter(s -> securityExpectedId.equals(s.getId())).findAny()
				.orElse(null);
		assertNotNull("Security " + securityname + " doesn't exist", security);
	}

	private SecurityDTO getSecurity(String securityName) {
		assertTrue(String.format("Unknown security \"%s\"", securityName),
				CONTEXT.securityMap.containsKey(securityName));
		UUID securityId = CONTEXT.securityMap.get(securityName).getId();
		return API.getSecurity(securityId);
	}

	private int getNumberOfExistingSecurities() {
		List<SecurityDTO> securityList = API.getAllSecurities();
		return securityList.size();
	}
}
