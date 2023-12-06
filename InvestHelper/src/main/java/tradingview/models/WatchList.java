package tradingview.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tradingview.utils.props.TestCaseProperty;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class WatchList {


    private static final Logger logger = LogManager.getLogger(WatchList.class);


    private String name;
    private List<Stock> stockList;


    public void addStock(Stock stock) {
        if (this.stockList == null) {
            this.stockList = new ArrayList<>();
        }

        if (!stockList.contains(stock)) {
            this.stockList.add(stock);
        }
    }


    @Override
    public String toString() {
        return "WatchList{" +
                "name='" + name + '\'' +
                ", stockList='" + stockList + '\'' +
                '}';
    }
}
