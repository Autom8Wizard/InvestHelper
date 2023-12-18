package testhooks;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import tradingview.utils.DataTransferStub;
import tradingview.utils.PropertyHandler;
import tradingview.utils.annotations.Configurator;
import tradingview.utils.annotations.NoTestData;
import tradingview.utils.logging.LoggingConfigurator;
import tradingview.utils.props.GlobalProperty;
import tradingview.utils.props.TestCaseProperty;
import tradingview.utils.testdataproviders.TestDataFactory;
import tradingview.utils.testdataproviders.TestDataLoader;
import tradingview.utils.testdataproviders.TestDataProvider;
import tradingview.utils.video.VideoRecorder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class TestHooks {

    private static final Logger logger = LogManager.getLogger(TestHooks.class);


    private boolean setupRequired;
    private boolean setupBackRequired;
    private boolean noTestData;
    @Getter
    private VideoRecorder videoRecorder;


    /**
     * Define Global Variables
     * This hook runs before test suite (once)
     * Calls loadDefaults() method which reads framework.settings file to define settings vars
     * <p>
     * Also, the logger is started under this hook (Log4j)
     */
    @BeforeAll
    static void a00() throws IOException {
        // load global props
        GlobalProperty.loadDefaults();

        // start logger
        LoggingConfigurator.configureLogProperties(LoggingConfigurator.getGlobalLoggingProperties());

        logger.info("Global Properties defined: " + GlobalProperty.getProperties());
    }


    /**
     * Check browser version and update/download webDriver
     */
    @BeforeEach
    public void a01() {
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
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        logger.info("WebDriver update/check finished.");
    }


    /**
     * Start separate logger for the current Test Case
     *
     * @param testInfo TestInfo
     * @throws FileNotFoundException File not found exception
     */
    @BeforeEach
    public void a02(TestInfo testInfo) throws FileNotFoundException {
        String testName = testInfo.getTestMethod().get().getName();
        LoggingConfigurator.configureLogProperties(
                PropertyHandler.mergeProperties(
                        LoggingConfigurator.getGlobalLoggingProperties(),
                        LoggingConfigurator.getTCLoggingProperties(testName)
                )
        );
        logger.trace("Logger for the test case '" + testName + "' started.");
    }


    /**
     * Define Hooks Restrictions
     * The hook runs before each method (test case)
     * The goal is to read method's annotations to check what hooks are required
     * Most hooks run by default (if no annotation specified), to change the default behavior
     * use the annotations listed below in the method
     *
     * @param t Test Info (test case)
     */
    @BeforeEach()
    public void a03(TestInfo t) {
        // get annotations
        Configurator configurationAnnotation = t.getTestMethod().get().getAnnotation(Configurator.class);
        NoTestData noTestDataAnnotation = t.getTestMethod().get().getAnnotation(NoTestData.class);

        // define variables in accordance with the specified annotations
        // configuration
        if (configurationAnnotation != null) {
            if (configurationAnnotation.setup()) {
                setupRequired = true;
            }
            if (configurationAnnotation.setupBack()) {
                setupBackRequired = true;
            }
        }

        // no test data
        if (noTestDataAnnotation != null) {
            noTestData = true;
        }
    }


    /**
     * Define Test Data
     * The hook runs before each method (test case)
     * The goal is to load the test data from the test data source (controlled by 'TESTDATA_PROVIDER' property in settings)
     * <p>
     * The result: 'TestCaseProperty' will be keeping test case's testing data
     *
     * @param testInfo Test Info instance
     */
    @BeforeEach()
    public void a04(TestInfo testInfo) throws FileNotFoundException {
        String testName = testInfo.getTestMethod().get().getName();
        GlobalProperty.setProperty("testName", testName);

        if (!noTestData) {
            TestDataFactory testDataFactory = TestDataLoader.createTestDataBySource(GlobalProperty.getProperty("TESTDATA_PROVIDER"));
            TestDataProvider dataProvider = testDataFactory.createTestDataProvider(testName);
            Map<String, String> testData = dataProvider.loadTestData();
            TestCaseProperty.loadTestCaseData(testData);
        } else {
            logger.warn("Current Test ('" + testName + "' is marked as the Test w/o Test Data). " +
                    "Test Data preparation step skipped.");
        }
        logger.info("Test Case Data defined: " + TestCaseProperty.getProperties());
    }


    /**
     * Create Setup for Test case
     * The hook runs for each method (test case)
     * The goal is to Prepare additional required Setup for Test Case
     * Can be enabled on the test case level (if additional setup required for the test case)
     */
    @BeforeEach()
    public void a05() {
        if (setupRequired) {
            logger.info("Test Case Setup started...");
            Setup.testCaseSetup();
            logger.info("Test Case Setup finished.");
        } else {
            logger.info("Setup is not required for the case.");
        }
    }


    /**
     * Start video recording for the Test Case
     *
     * @param testInfo TestInfo
     */
    @BeforeEach
    public void a06(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().get().getName();
        this.videoRecorder = new VideoRecorder(testName);
        // start the recording of the test
        this.videoRecorder.startRecording();
        // save video recorder instance in temporary object holder class (can be invoked from testListeners)
        DataTransferStub.setObject(this.videoRecorder);
        logger.trace("Video Recording for the test case '" + testName + "' started.");
    }


    /**
     * Remove Setup created during Test Case execution or specific Setup created in Before Method
     * The hook runs for each method (test case)
     * The goal is to Remove additionally Created Setup for Test Case
     * Can be enabled on the test case level (if there is setup required to be removed for the test case)
     */
    @AfterEach()
    public void a07() {
        if (setupBackRequired) {
            logger.info("Test Case SetupBack started...");
            SetupBack.testCaseSetupBack();
            logger.info("Test Case SetupBack finished.");
        } else {
            logger.info("SetupBack is not required for the case.");
        }
    }


    /**
     * Close WebDriver
     * The hook runs after each method (test case)
     * The goal is to close active webDriver is he was initialized in the @Before method
     */
    @AfterEach
    public void a08() {
        Selenide.closeWebDriver();
    }
}
