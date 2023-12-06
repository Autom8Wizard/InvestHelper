package tradingview.utils.testdataproviders;

import java.io.FileNotFoundException;
import java.util.Map;

public interface TestDataProvider {

    Map<String, String> loadTestData() throws FileNotFoundException;

}
