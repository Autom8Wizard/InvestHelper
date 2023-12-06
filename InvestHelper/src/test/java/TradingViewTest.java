import org.junit.jupiter.api.DisplayName;
import testhooks.TestHooks;
import tradingview.models.Stock;
import org.junit.jupiter.api.Test;
import tradingview.models.WatchList;
import tradingview.processes.StocksManagement;
import tradingview.utils.props.TestCaseProperty;

import java.util.Arrays;
import java.util.List;

public class TradingViewTest extends TestHooks {


    @Test
    @DisplayName("Get Top Gainers Stocks for the last trading day")
    public void tc_1() {
        Integer stocksQuantity = TestCaseProperty.getIntData("stocks_quantity");
        List<Stock> topGainersStockList = StocksManagement.getListOfTopGainersStocks(stocksQuantity);
    }


    @Test
    @DisplayName("Add Stocks to the existing Watchlist")
    public void tc_2() {
        Stock stockToAdd1 = Stock.buildStockByTestDataIndex(0);
        Stock stockToAdd2 = Stock.buildStockByTestDataIndex(1);
        String watchListName = TestCaseProperty.getData("watchlist_name");

        WatchList watchList = WatchList
                .builder()
                .withName(watchListName)
                .build();

        WatchList updatedWatchList = StocksManagement.addStocksToTheWatchList(watchList, Arrays.asList(stockToAdd1, stockToAdd2));
    }
}
