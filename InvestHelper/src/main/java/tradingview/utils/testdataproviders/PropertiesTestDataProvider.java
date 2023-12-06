package tradingview.utils.testdataproviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tradingview.utils.PropertyHandler;
import tradingview.utils.props.GlobalProperty;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesTestDataProvider implements TestDataProvider {

    private static final Logger logger = LogManager.getLogger(PropertiesTestDataProvider.class);

    private final String scenarioId;

    public PropertiesTestDataProvider(String scenarioId) {
        this.scenarioId = scenarioId;
    }


    /**
     * Load test data from the properties file
     *
     * @return Scenario test data map
     * @throws FileNotFoundException Thrown if the property file not found
     */
    @Override
    public Map<String, String> loadTestData() throws FileNotFoundException {

        Map<String, String> scenarioTestDataMap = new HashMap<>();

        String testDataFilePath = GlobalProperty.getProperty("TESTDATA_PATH") + scenarioId + ".properties";

        Properties testDataProps = new Properties();
        PropertyHandler.loadProperties(testDataProps, testDataFilePath);

        for (Object key: testDataProps.keySet()) {
            scenarioTestDataMap.put((String) key, testDataProps.getProperty((String) key));
        }

        return scenarioTestDataMap;
    }
}
