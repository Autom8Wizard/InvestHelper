package tradingview.utils.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationBuilder;
import tradingview.utils.PropertyHandler;
import tradingview.utils.props.GlobalProperty;

import java.io.FileNotFoundException;
import java.util.Properties;

public class LoggingConfigurator {


    public static Properties getGlobalLoggingProperties() throws FileNotFoundException {
        Properties logProps = PropertyHandler.createProperties(GlobalProperty.getProperty("LOGGER_PROPERTIES"));
        logProps.setProperty("appender.rolling.fileName", GlobalProperty.getProperty("LOG_FILE"));

        return logProps;
    }


    public static Properties getTCLoggingProperties(String logToFile) throws FileNotFoundException {
        Properties logProps = PropertyHandler.createProperties(GlobalProperty.getProperty("TC_LOGGER_PROPERTIES"));
        logProps.setProperty("appender.tcFileAppender.fileName", GlobalProperty.getProperty("LOG_FILE_PATH") + logToFile + ".log");

        return logProps;
    }


    public static void configureLogProperties(Properties properties) {
        //PropertyConfigurator.configure(properties);
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = new PropertiesConfigurationBuilder()
                .setConfigurationSource(ConfigurationSource.NULL_SOURCE)
                .setRootProperties(properties).setLoggerContext(context).build();
        context.setConfiguration(config);
        Configurator.initialize(config);
    }


}
