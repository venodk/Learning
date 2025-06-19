package cooper;

import java.util.List;
import java.util.concurrent.Future;

public interface ClientApi {
    /**
     * @return List with all available clients.
     */
    public Future<List<Client>> getClients();
}
