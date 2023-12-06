package tradingview.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import dev.failsafe.internal.util.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class TopGainerStocksMarketsPage extends BaseWebUI implements IndependentPage {


    private static final Logger logger = LogManager.getLogger(TopGainerStocksMarketsPage.class);


    private final SelenideElement pageId = $x("//button[@data-main-menu-root-track-id = 'markets' and text() = 'Markets']//ancestor::div[@class = 'tv-main']//a[contains(@class, 'category-tab--active')]/div[text() = 'Top gainers']");
    private final SelenideElement spamPopUpCloseButton = $x("//div[contains(@class, 'tv-dialog__close')]");
    private final SelenideElement dataTable = $x("//div[contains(@class, 'tableWrap')]//table[contains(@class, 'table') and not(contains(@class,  'tableSticky'))]");
    private final ElementsCollection dataTableRows = $$(By.xpath("//div[contains(@class, 'tableWrap')]//table[contains(@class, 'table') and not(contains(@class,  'tableSticky'))]//tbody//tr"));
    private final ElementsCollection dataTableHeaders = $$(By.xpath("//div[contains(@class, 'tableWrap')]//table[contains(@class, 'table') and contains(@class,  'tableSticky')]//th//div[contains(@class, 'headCellTitle')]"));
    // row number (1,2,3...etc) should be passed to the template
    protected String dataTableRowTemplate = "//div[contains(@class, 'tableWrap')]//table[contains(@class, 'table') and not(contains(@class,  'tableSticky'))]//tbody//tr[%s]";
    // row number (1,2,3...etc) and row column (same 1,2,3,... etc) should be passed to the template
    protected String dataTableRowColumnTemplate = "//div[contains(@class, 'tableWrap')]//table[contains(@class, 'table') and not(contains(@class, 'tableSticky'))]//tbody//tr[%s]//td[%s]";
    protected String dataTableRowColumnTickerTemplate = dataTableRowColumnTemplate + "//a[contains(@class, 'tickerNameBox')]";


    @Override
    public boolean isThisPage() {
        return WebDriverRunner.url().toLowerCase()
                .contains(TOP_GAINERS_STOCKS_MARKETS_PAGE_URL.toLowerCase()) && pageId.isDisplayed();
    }


    public TopGainerStocksMarketsPage closeSpamPopUp() {
        spamPopUpCloseButton.click();
        spamPopUpCloseButton.shouldBe(Condition.not(Condition.visible));
        return this;
    }


    public TopGainerStocksMarketsPage scrollToDataTable() {
        dataTable.scrollTo();
        return this;
    }


    private int getNumberOfDisplayedDataRows() {
        return dataTableRows.size();
    }


    private List<String> getListOfDataTableHeaders() {
        List<String> tableColumnsList = new ArrayList<>();
        dataTableHeaders.forEach(selEl -> tableColumnsList.add(selEl.toWebElement().getAttribute("innerText")));
        logger.info("List of data table headers returned: " + tableColumnsList);
        return tableColumnsList;
    }


    public Map<Integer, Map<String, String>> getTableDataMap(int numberOfTableRows) {
        int numberOfRowsIterator = numberOfTableRows > 0 ? numberOfTableRows : getNumberOfDisplayedDataRows();

        // get headers
        List<String> headers = getListOfDataTableHeaders();
        Map<Integer, Map<String, String>> configurationTableMap = new HashMap<>();
        for (int i = 0; i < numberOfRowsIterator; i++) {
            Map<String, String> configurationRowMap = new HashMap<>();

            // get row
            String rowXpath = String.format(dataTableRowTemplate, i + 1);
            $x(rowXpath).shouldBe(Condition.visible);

            // put ticker name separately
            String columnTickerText = $x(String.format(dataTableRowColumnTickerTemplate, i + 1, 1)).toWebElement().getText();
            configurationRowMap.put("ticker", columnTickerText);

            for (int j = 0; j < headers.size(); j++) {
                String headerName = headers.get(j);
                String rowColumnXpath = String.format(dataTableRowColumnTemplate, i + 1, j + 1);

                WebElement rowColumn = $x(rowColumnXpath).toWebElement();
                String columnText = rowColumn.getText();

                configurationRowMap.put(headerName.toLowerCase(), columnText);
            }
            configurationTableMap.put((i + 1), configurationRowMap);
        }
        logger.info("Data results table map returned: " + configurationTableMap);
        return configurationTableMap;
    }


    public Map<Integer, Map<String, String>> getTableDataMap() {
        return getTableDataMap(100);
    }


}
