package name.lattuada.trading.tests.steps;

import org.hamcrest.Matchers;

import io.cucumber.java.en.Then;

/**
 * 
 * Provides Cucumber based middle layer implementation of shared testing methods, 
 * which are not dependent on particular REST API endpoint. 
 * 
 * Methods in this class can be typically used for testing any services provided by various controllers.
 * 
 * @author michal masarik
 *
 */
public class GeneralStepDefinitions implements IStepDefinitions{
	
	@Then("server responds in {long} miliseconds")
	public void serverRespondsInMiliseconds(Long miliseconds) {
		LOGGER.info("Response time was " + CONTEXT.response.time() + " ms.");
		CONTEXT.response.then().time(Matchers.lessThan(miliseconds));
	}	

	@Then("server responds with code {int}")
	public void serverRespondsWithCode(Integer code) {
		CONTEXT.response.then().assertThat().statusCode(code);
	}
}
