package tradingview.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class SharedContext {

    private boolean setupRequired;
    private boolean setupBackRequired;
    private boolean noTestData;
}
