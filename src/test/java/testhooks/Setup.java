package testhooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tradingview.utils.props.GlobalProperty;

public class Setup {


    private static final Logger logger = LogManager.getLogger(Setup.class);


    public static void testCaseSetup() {
        String tcId = GlobalProperty.getCurrentTestName() == null ? "" : GlobalProperty.getCurrentTestName();
        switch (tcId.toLowerCase()) {
            case "tc_1":
                tc1_setup();
                break;
            case "tc_2":
                tc2_setup();
                break;
            default:
                logger.info(tcId + " - Setup method is not found.");
        }
        logger.info(tcId + " - Setup method has been executed.");
    }


    /**
     * TC_1
     */
    private static void tc1_setup() {
        logger.info("Just custom setup for test case tc_1 started");
    }


    /**
     * TC_2
     */
    private static void tc2_setup() {
        logger.info("Just custom setup for test case tc_2 started");
    }


}