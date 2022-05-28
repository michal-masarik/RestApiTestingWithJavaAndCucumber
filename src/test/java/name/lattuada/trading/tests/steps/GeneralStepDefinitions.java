package name.lattuada.trading.tests.steps;

import org.hamcrest.Matchers;

import io.cucumber.java.en.Then;

/**
 * Provides Cucumber based middle layer implementation of testing methods
 * not connected with particular service. 
 * This methods are typically shared by all APIs
 * @author michal masarik
 *
 */
public class GeneralStepDefinitions implements IStepDefinitions{
	
	@Then("server responds in {long} miliseconds")
	public void server_responds_in_miliseconds(Long miliseconds) {
		logger.info("Response time was " + context.response.time() + " ms.");
		context.response.then().time(Matchers.lessThan(miliseconds));
	}	

	@Then("server responds with code {int}")
	public void server_responds_with_code(Integer code) {
		context.response.then().assertThat().statusCode(code);
	}
}
