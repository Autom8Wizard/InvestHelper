package tradingview.utils.testdataproviders;

public class PropertiesTestDataFactory implements TestDataFactory {

    @Override
    public TestDataProvider createTestDataProvider(String scenario) {
        return new PropertiesTestDataProvider(scenario);
    }
}
