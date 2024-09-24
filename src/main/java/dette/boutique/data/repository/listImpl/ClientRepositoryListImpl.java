package dette.boutique.data.repository.listImpl;

import dette.boutique.core.database.impl.RepositoryListImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.ClientRepository;

public class ClientRepositoryListImpl extends RepositoryListImpl<Client> implements ClientRepository {
    @Override
    public Client findByTel(String telephone) {
        return data.stream()
                .filter(client -> client.getTelephone().compareTo(telephone) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean insertWithoutUser(Client client) {
        data.add(client);
        return true;
    }

    @Override
    public boolean updateUserForClient(Client client) {
        client.setUser(client.getUser());
        return true;
    }

}
