package tradingview.utils.testdataproviders;

public class TestDataLoader {

    /**
     * Get Test Data Factory instance based on the test data source
     *
     * @param source Test Data source (from the settings)
     * @return Test Data Factory instance
     */
    public static TestDataFactory createTestDataBySource(String source) {
        if (source.equalsIgnoreCase("properties")) return new PropertiesTestDataFactory();
        else throw new RuntimeException("'" + source + "' is unknown test data source");
    }

}
