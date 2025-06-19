package cooper;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BalanceServiceImpl implements BalanceService {

    private final ClientApi clientApi;
    private final TradeApi tradeApi;

    public BalanceServiceImpl(ClientApi clientApi, TradeApi tradeApi) {
        this.clientApi = clientApi;
        this.tradeApi = tradeApi;
    }

    @Override
    public Future<Double> getClientTotalTradeVolume(String clientId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Trade> trades = tradeApi.getTrades().get();
                return trades.stream()
                        .filter(trade -> trade.clientId().equals(clientId))
                        .mapToDouble(trade -> trade.pricePerUnit() * trade.quantity())
                        .sum();
            } catch (Exception e) {
                throw new RuntimeException("Failed to calculate trade volume for client: " + clientId, e);
            }
        });
    }

    @Override
    public Future<TradeReport> createTradeReport(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Client> clients = clientApi.getClients().get();
                Set<String> clientIdsFromCountry = clients.stream()
                        .filter(client -> country.equalsIgnoreCase(client.getCountry()))
                        .map(Client::getClientId)
                        .collect(Collectors.toSet());

                List<Trade> trades = tradeApi.getTrades().get();
                double totalVolume = trades.stream()
                        .filter(trade -> clientIdsFromCountry.contains(trade.clientId()))
                        .mapToDouble(trade -> trade.pricePerUnit() * trade.quantity())
                        .sum();

                return new TradeReport(country, clientIdsFromCountry.size(), totalVolume);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create trade report for country: " + country, e);
            }
        });
    }
}

