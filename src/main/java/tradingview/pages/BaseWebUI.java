package tradingview.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseWebUI {

    private static final Logger logger = LogManager.getLogger(BaseWebUI.class);


    protected final String TRADING_VIEW_PAGE_URL = "https://www.tradingview.com/";
    protected final String LOGIN_PAGE_URL = "https://accounts.google.com/v3/signin/";
    protected final String TOP_GAINERS_STOCKS_MARKETS_PAGE_URL = "https://www.tradingview.com/markets/stocks-usa/market-movers-gainers/";

    ///////////////* Timeouts *///////////////////////////////////
    protected static int timeout = 5;
    protected static int timeoutShort = 3;
    protected static int timeoutLong = 10;

}
