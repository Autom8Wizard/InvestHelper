package tradingview.utils.props;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TestCaseProperty {


    private static final Logger logger = LogManager.getLogger(TestCaseProperty.class);
    private static final Properties TEST_CASE_PROPERTY = new Properties();


    public static Properties get() {
        return TEST_CASE_PROPERTY;
    }


    public static void loadTestCaseData(Map<String, String> testCaseData) {
        clear();

        for (Map.Entry<String, String> entry : testCaseData.entrySet()) {
            TestCaseProperty.setProperty(entry.getKey(), entry.getValue());
        }
    }


    public static void clear() {
        TEST_CASE_PROPERTY.clear();
    }


    private static String getProperty(String key) {
        String value = TEST_CASE_PROPERTY.getProperty(key);
        if (value == null) {
            value = TEST_CASE_PROPERTY.getProperty(key.toUpperCase());
            if (value == null) {
                value = TEST_CASE_PROPERTY.getProperty(key.toLowerCase());
            }
        }
        return value;
    }


    public static void setProperty(String key, String value) {
        TEST_CASE_PROPERTY.setProperty(key, value);
    }


    public static Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();

        if (!TEST_CASE_PROPERTY.keySet().isEmpty()) {
            for (Object key : TEST_CASE_PROPERTY.keySet()) {
                props.put(key.toString(), TEST_CASE_PROPERTY.getProperty(key.toString()));
            }
        }
        return props;
    }


    public static String getData(String key) {
        return getProperty(key);
    }


    public static String getData(String key, int index) {
        List<String> values = getList(key);
        try {
            return values.get(index);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("Element by index " + index + " not found in the test data file for the key '" + key + "'. Empty value returned.");
            return "";
        }
    }


    public static Integer getIntData(String key) {
        String data = getData(key);
        return data == null || data.equalsIgnoreCase("null") || data.isEmpty() ? null : Integer.parseInt(data);
    }


    public static Integer getIntData(String key, int index) {
        String data = getData(key, index);
        return data == null || data.equalsIgnoreCase("null") || data.isEmpty() ? null : Integer.parseInt(data);
    }


    public static List<String> getList(String key) {
        String value = getProperty(key);

        if (value == null) {
            logger.error("Key '" + key + "' is unknown (probably such key is missed in the test data file). Empty list returned..");
            return new ArrayList<>();
        }

        String[] parsedArray = value.split("(?<!\\\\),"); // split by ',' and ignore '\,'
        List<String> parsedList = new ArrayList<>();
        for (String s : parsedArray) {
            parsedList.add(s.replaceAll("\\\\,", ",").trim()); // replace '\,' with ',' if it exists
        }
        return parsedList;
    }


}