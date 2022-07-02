package name.lattuada.trading.tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;

/**
 * Entry point for Cucumber tests
 * 
 * @author michal masarik
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty" }, features = "src/test/resources/features")
@CucumberContextConfiguration
public class CucumberTest {
}
