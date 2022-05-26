package name.lattuada.trading.tests.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.java.Before;
import name.lattuada.trading.controller.OrderController;
import name.lattuada.trading.tests.CucumberTest;
import name.lattuada.trading.tests.TestContext;
import name.lattuada.trading.tests.api.ApiFacade;

/**
 * This interface provides basic functionality shared for all 
 * step definitions classes. For example it enables {@link ApiFacade},
 * {@link TestContext} and logger or it can provide other functionalities to
 * be available without necessity to create them in implemented classes.
 * 
 * Current architecture split test step definitions based on SUT functionality.
 * Each class represents tests steps connected with particular controller and its REST API endpoint
 * Eg. {@link OrderStepDefinitions} represents test actions connected with calling {@link OrderController} etc.
 * This solution have currently the most benefits for tests readability. Negative
 * side of solution is that during switching of step definition classes in complex test case, we are loosing 
 * context for storing test attributes. For this purpose is implemented {@link TestContext} which 
 * serves for temporary caching.
 * 
 * This is also place, where various @before and @after methods can be defined for shared usage.
 * 
 * @author michal masarik
 *
 */
public interface IStepDefinitions {
	
	@Before
	private void cleanTestContext(){
		context.clean();	
	}

	static final ApiFacade api = new ApiFacade();
	static final TestContext context = TestContext.getInstance();
	static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);
}
