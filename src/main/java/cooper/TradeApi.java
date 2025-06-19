package cooper;

import java.util.List;
import java.util.concurrent.Future;

public interface TradeApi {
    /**
     * @return List with all available trades.
     */
    public Future<List<Trade>> getTrades();
}
