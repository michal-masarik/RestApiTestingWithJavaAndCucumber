package name.lattuada.trading.tests.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.controller.SecurityController;
import name.lattuada.trading.model.dto.SecurityDTO;

/**
 * Provides Cucumber based middle layer implementation of testing methods connected with Security endpoint.
 * This methods are typically processed by {@link SecurityController} and available on /api/securities for REST API
 * 
 * @author michal masarik
 *
 */
public class SecurityStepDefinitions implements IStepDefinitions {

	private int numberOfSecurities = 0;

	@When("security {string} is created")
	@Given("one security {string} is created")
	public void one_security_is_created(String securityName) {
		SecurityDTO securityDTO = new SecurityDTO();
		securityDTO.setName(securityName);
		SecurityDTO returnedSecurity = api.createSecurity(securityDTO);
		context.securityMap.put(securityName, returnedSecurity);
		//logger.info("Security created: {}", returnedSecurity);
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

	private void assertSecurityNotInList(String securityname, List<SecurityDTO> securityList) {
		UUID securityExpectedId = context.securityMap.get(securityname).getId();
		SecurityDTO security = securityList.stream().filter(s -> securityExpectedId.equals(s.getId()))
				.findAny().orElse(null);
		assertNotNull("Security " + securityname + " doesn't exist", security);
	}

	private SecurityDTO getSecurity(String securityName) {
		assertTrue(String.format("Unknown security \"%s\"", securityName), context.securityMap.containsKey(securityName));
		UUID securityId = context.securityMap.get(securityName).getId();
		return api.getSecurity(securityId);
	}

	private int getNumberOfExistingSecurities() {
		List<SecurityDTO> securityList = api.getAllSecurities();
		return securityList.size();
	}
}
