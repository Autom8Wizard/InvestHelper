package tradingview.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyHandler {


    private static final Logger logger = LogManager.getLogger(PropertyHandler.class);


    /**
     * Load properties from the .properties File to the Properties instance
     *
     * @param properties Instance of the Properties class
     * @param path       Path to .properties File
     * @throws FileNotFoundException Thrown if the .properties file not found
     */
    public static void loadProperties(Properties properties, String path) throws FileNotFoundException {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (FileNotFoundException e) {
            logger.error("File: " + path + " not found");
            logger.error(e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * Create Properties instance and load .properties file to it
     *
     * @param path Path to .properties file
     * @return Properties instance
     * @throws FileNotFoundException Thrown if the .properties file not found
     */
    public static Properties createProperties(String path) throws FileNotFoundException {
        Properties props = new Properties();
        loadProperties(props, path);

        return props;
    }


    /**
     * Merge a few Properties instances to one Properties instance
     *
     * @param properties Collections of properties
     * @return Properties instance (merge of the given properties)
     */
    public static Properties mergeProperties(Properties... properties) {
        Properties finalProperties = new Properties();
        for (Properties property : properties) {
            finalProperties.putAll(property);
        }

        return finalProperties;
    }


}