package testhooks;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import tradingview.utils.PropertyHandler;
import tradingview.utils.logging.LoggingConfigurator;
import tradingview.utils.props.GlobalProperty;
import tradingview.utils.props.TestCaseProperty;
import tradingview.utils.testdataproviders.TestDataFactory;
import tradingview.utils.testdataproviders.TestDataLoader;
import tradingview.utils.testdataproviders.TestDataProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;


public class TestHooks {


    private static final Logger logger = LogManager.getLogger(TestHooks.class);


    private void setUp() {
        String browser = GlobalProperty.getProperty("BROWSER");
        logger.info("Auto update webDriver started.. Browser: " + browser);

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            Configuration.browser = "chrome";
            logger.info("Chrome webDriver checked.");
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            Configuration.browser = "firefox";
            logger.info("FireFox webDriver checked.");
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            Configuration.browser = "edge";
            logger.info("Edge webDriver checked.");
        } else {
            String errorMsg = "Browser '" + browser + "' is unknown.";
            logger.fatal(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        Configuration.headless = false;
        Configuration.browserSize = "1920x1080";
        logger.info("WebDriver update/check finished.");
    }


    @BeforeAll
    static void defineGlobalProperties() throws IOException {
        // load global props
        GlobalProperty.loadDefaults();

        // start logger
        LoggingConfigurator.configureLogProperties(LoggingConfigurator.getGlobalLoggingProperties());

        logger.info("Global Properties defined: " + GlobalProperty.getProperties());
    }


    @BeforeEach
    public void startTestCaseLogger(TestInfo testInfo) throws IOException {
        String testName = testInfo.getDisplayName();

        LoggingConfigurator.configureLogProperties(
                PropertyHandler.mergeProperties(
                        LoggingConfigurator.getGlobalLoggingProperties(),
                        LoggingConfigurator.getTCLoggingProperties(testName)
                )
        );

        logger.trace("Logger for the test case '" + testName + "' started.");
    }


    @BeforeEach()
    public void defineTestData(TestInfo testInfo) throws FileNotFoundException {
        String testName = testInfo.getTestMethod().get().getName();
        GlobalProperty.setProperty("testName", testName);

        TestDataFactory testDataFactory = TestDataLoader.createTestDataBySource(GlobalProperty.getProperty("TESTDATA_PROVIDER"));
        TestDataProvider dataProvider = testDataFactory.createTestDataProvider(testName);
        Map<String, String> testData = dataProvider.loadTestData();
        TestCaseProperty.loadTestCaseData(testData);

        logger.info("Test Case Data defined: " + TestCaseProperty.getProperties());
    }


    @BeforeEach
    public void init() {
        setUp();
    }


    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
