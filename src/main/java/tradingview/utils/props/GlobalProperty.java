package tradingview.utils.props;

import tradingview.utils.PropertyHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

public class GlobalProperty extends PropertyHandler {


    private static final Properties GLOBAL_PROPERTY = new Properties();


    private static void loadSettings(Properties properties) throws FileNotFoundException {
        Properties temp_props = new Properties();
        loadProperties(temp_props, "framework.settings");

        properties.setProperty("PROJECT_PATH", System.getProperty("user.dir") + File.separator);
        properties.setProperty("TESTDATA_PATH", temp_props.getProperty("TESTDATA_PATH").replaceAll("/", Matcher.quoteReplacement(File.separator)) + File.separator);
        properties.setProperty("TESTDATA_PROVIDER", temp_props.getProperty("TESTDATA_PROVIDER", "properties"));

        properties.setProperty("BROWSER", temp_props.getProperty("BROWSER", temp_props.getProperty("DEFAULT_BROWSER", "chrome")));
        properties.setProperty("LOGGER_PROPERTIES", temp_props.getProperty("LOGGER_SETTINGS_FILE_PATH").replaceAll("/", Matcher.quoteReplacement(File.separator)) + File.separator + temp_props.getProperty("LOGGER_SETTINGS_FILENAME"));
        properties.setProperty("TC_LOGGER_PROPERTIES", temp_props.getProperty("LOGGER_SETTINGS_FILE_PATH").replaceAll("/", Matcher.quoteReplacement(File.separator)) + File.separator + temp_props.getProperty("TC_LOGGER_SETTINGS_FILENAME"));
        properties.setProperty("LOG_FILE_PATH", temp_props.getProperty("LOG_FILE_PATH").replaceAll("/", Matcher.quoteReplacement(File.separator)) + File.separator);
        properties.setProperty("LOG_FILE", properties.getProperty("LOG_FILE_PATH") + temp_props.getProperty("LOG_TO_FILE"));

        properties.setProperty("USER_LOGIN", temp_props.getProperty("USER_LOGIN"));
        properties.setProperty("USER_PASSWORD", temp_props.getProperty("USER_PASSWORD"));
    }


    public static void loadDefaults() throws FileNotFoundException {
        clear();
        loadSettings(GLOBAL_PROPERTY);
    }


    public static Properties get() {
        return GLOBAL_PROPERTY;
    }


    public static void clear() {
        GLOBAL_PROPERTY.clear();
    }


    public static String getProperty(String key, String defaultValue) {
        String value = GLOBAL_PROPERTY.getProperty(key, defaultValue);
        if (value == null) {
            value = GLOBAL_PROPERTY.getProperty(key.toUpperCase(), defaultValue);
            if (value == null) {
                value = GLOBAL_PROPERTY.getProperty(key.toLowerCase(), defaultValue);
            }
        }
        return value;
    }


    public static String getProperty(String key) {
        return getProperty(key, null);
    }


    public static void setProperty(String key, String value) {
        GLOBAL_PROPERTY.setProperty(key, value);
    }


    public static void removeProperty(String key) {
        GLOBAL_PROPERTY.remove(key);
    }


    public static Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();

        if (!GLOBAL_PROPERTY.keySet().isEmpty()) {
            for (Object key : GLOBAL_PROPERTY.keySet()) {
                props.put(key.toString(), GLOBAL_PROPERTY.getProperty(key.toString()));
            }
        }
        return props;
    }


    public static String getCurrentTestName() {
        return getProperty("testName");
    }
}