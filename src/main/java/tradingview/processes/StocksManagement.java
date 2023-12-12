package tradingview.processes;

import dev.failsafe.internal.util.Assert;
import tradingview.models.Stock;
import tradingview.models.WatchList;
import tradingview.pages.ProfilePage;
import tradingview.pages.TopGainerStocksMarketsPage;
import tradingview.pages.TradingViewPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tradingview.utils.props.GlobalProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StocksManagement {


    private static final Logger logger = LogManager.getLogger(StocksManagement.class);


    /**
     * Get list of top gainers stocks for the last trading day
     *
     * @param number - number of first top gainers stocks (by default 100)
     * @return list of Stocks
     */
    public static List<Stock> getListOfTopGainersStocks(Integer number) {
        logger.trace("Method started..");

        TopGainerStocksMarketsPage topGainerStocksMarketsPage = new TradingViewPage()
                .navigateToTopGainerStocksMarketsPage()
                .scrollToDataTable();

        Map<Integer, Map<String, String>> topGainersStocksMap = topGainerStocksMarketsPage.getTableDataMap(number);
        List<Stock> topGainersStockList = topGainersStocksMap
                .values()
                .stream()
                .map(el -> Stock.builder()
                        .withTicker(el.get("ticker"))
                        .withSector(el.get("sector"))
                        .withPrice(el.get("price"))
                        .withVolume(el.get("volume"))
                        .withMarketCap(el.get("market cap"))
                        .withAnalyticsRecommendation(el.get("analyst rating"))
                        .build())
                .limit(number)
                .toList();

        logger.info("List of top gainers stocks returned: " + topGainersStockList + ". Happy trading!");
        logger.trace("Method finished..");
        return topGainersStockList;
    }


    /**
     * Get list of top gainers stocks for the last trading day
     *
     * @return list of Stocks (by default 100)
     */
    public static List<Stock> getListOfTopGainersStocks() {
        return getListOfTopGainersStocks(100);
    }


    /**
     * Add Stock to the WatchList
     *
     * @param watchList WatchList instance
     * @param stockList List of Stocks to add
     * @return updated Watchlist
     */
    public static WatchList addStocksToTheWatchList(WatchList watchList, List<Stock> stockList) {
        logger.trace("Method started..");

        ProfilePage profilePage = new TradingViewPage().clickOnSignInButton()
                .clickOnSignInLinkedText()
                .clickOnSignInWithGoogle()
                .fillEmail(GlobalProperty.getProperty("USER_LOGIN"))
                .fillPassword(GlobalProperty.getProperty("USER_PASSWORD"));

        // validate watchlist name in web ui
        Assert.isTrue(profilePage.getWatchListName().equals(watchList.getName()),
                "WatchList name in WebUi is unexpected. Expected: " + watchList.getName());

        profilePage.clickOnAddSymbolButton();
        for (Stock stock : stockList) {
            profilePage
                    .fillSymbol(stock.getTicker())
                    .clickOnSymbol(stock.getTicker(), stock.getStockExchange());
            logger.trace("Resuming method..");

            // update watchlist
            watchList.addStock(stock);
        }
        profilePage.clickOnAddSymbolPopUpCloseButton();

        logger.info("List of stocks: " + stockList + " successfully added into WatchList: " + watchList);
        logger.trace("Method finished..");
        return watchList;
    }


    /**
     * Add Stock to the existing WatchList
     *
     * @param watchList WatchList instance
     * @param stock     Stock instance
     * @return updated WatchList
     */
    public static WatchList addStockToTheWatchList(WatchList watchList, Stock stock) {
        logger.trace("Method started..");

        addStocksToTheWatchList(watchList, Collections.singletonList(stock));
        logger.trace("Resuming method..");

        logger.info("Stock: " + stock + " successfully added into WatchList: " + watchList);
        logger.trace("Method finished..");
        return watchList;
    }
}
