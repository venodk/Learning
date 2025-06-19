package cooper;

import java.util.concurrent.Future;

public interface BalanceService {

    /**
     * @param clientId
     * @return total amount paid by a client for all their traded units.
     */
    Future<Double> getClientTotalTradeVolume(String clientId);

    /**
     * @param country country of which you want to create a report
     * @return TradeReport for a country which includes the total number of clients
     * and the total amount paid for all the relevant trades.
     */
    Future<TradeReport> createTradeReport(String country);
}