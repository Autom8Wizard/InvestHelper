package testhooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tradingview.utils.props.GlobalProperty;

public class SetupBack {


    private static final Logger logger = LogManager.getLogger(SetupBack.class);


    public static void testCaseSetupBack() {
        String tcId = GlobalProperty.getCurrentTestName() == null ? "" : GlobalProperty.getCurrentTestName();
        switch (tcId.toLowerCase()) {
            case "tc_1":
                tc1_setupBack();
                break;
            case "tc_2":
                tc2_setupBack();
                break;
            default:
                logger.info(tcId + " - SetupBack method is not found.");
        }
        logger.info(tcId + " - SetupBack method has been executed.");
    }


    /**
     * TC_1
     */
    private static void tc1_setupBack() {
        logger.info("Just custom setupBack for test case tc_1 started");
    }


    /**
     * TC_2
     */
    private static void tc2_setupBack() {
        logger.info("Just custom setupBack for test case tc_2 started");
    }

}