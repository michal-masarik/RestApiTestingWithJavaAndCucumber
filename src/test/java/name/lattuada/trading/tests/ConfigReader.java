package name.lattuada.trading.tests;

import java.io.FileReader;
import java.util.Properties;

/**
 * This class reads test parameters (e.g. base URL) from a configuration file
 * 
 * @author michal masarik
 *
 */
public class ConfigReader {

	private static ConfigReader singleton = null;
	private static final String FILE_LOCATION = "src/test/resources/test.properties";

	private ConfigReader() {
	}

	/**
	 * This method is used for instantiating of {@link ConfigReader}
	 * 
	 * @return singleton instance of {@link ConfigReader}
	 */
	public static ConfigReader getInstance() {
		if (singleton == null) {
			singleton = new ConfigReader();
		}
		return singleton;
	}

	/**
	 * Method returns property value based on property key from configuration file
	 * 
	 * @param propertyName property key
	 * @return property value
	 */
	public String getProperty(String propertyName) {
		Properties properties = null;
		try {
			FileReader reader = new FileReader(FILE_LOCATION);
			properties = new Properties();
			properties.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties.getProperty(propertyName);
	}
}
