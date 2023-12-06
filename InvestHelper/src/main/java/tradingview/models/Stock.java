package tradingview.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tradingview.utils.props.TestCaseProperty;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Stock {


    private static final Logger logger = LogManager.getLogger(Stock.class);


    private String ticker;
    private String stockExchange;
    private String marketCap;
    private String price;
    private String volume;
    private String sector;
    private String analyticsRecommendation;


    public static Stock buildStockByTestDataIndex(int index) {
        String ticker = (TestCaseProperty.getData("ticker") != null) ? TestCaseProperty.getData("ticker", index) : null;
        String stockExchange = (TestCaseProperty.getData("stock_exchange") != null) ? TestCaseProperty.getData("stock_exchange", index) : null;
        String marketCap = (TestCaseProperty.getData("market_cap") != null) ? TestCaseProperty.getData("market_cap", index) : null;
        String price = (TestCaseProperty.getData("price") != null) ? TestCaseProperty.getData("price", index) : null;
        String volume = (TestCaseProperty.getData("volume") != null) ? TestCaseProperty.getData("volume", index) : null;
        String sector = (TestCaseProperty.getData("sector") != null) ? TestCaseProperty.getData("sector", index) : null;
        String analyticsRecommendation = (TestCaseProperty.getData("analytics_recommendation") != null) ? TestCaseProperty.getData("analytics_recommendation", index) : null;

        Stock stock = Stock.builder()
                .withTicker(ticker)
                .withStockExchange(stockExchange)
                .withMarketCap(marketCap)
                .withPrice(price)
                .withVolume(volume)
                .withSector(sector)
                .withAnalyticsRecommendation(analyticsRecommendation)
                .build();

        logger.info("Stock object has been created for the TestData Index '" + index + "': " + stock);
        return stock;
    }


    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", marketCap='" + marketCap + '\'' +
                ", exchange='" + stockExchange + '\'' +
                ", sector='" + sector + '\'' +
                ", analyticsRecommendation='" + analyticsRecommendation + '\'' +
                '}';
    }
}
