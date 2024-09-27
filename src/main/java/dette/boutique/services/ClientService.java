package dette.boutique.services;

import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.ClientRepository;

public class ClientService implements Item<Client> {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void create(Client client) {
        clientRepository.insert(client);
    }

    public boolean updateUserForClient(Client client) {
        clientRepository.updateUserForClient(client);
        return true;
    }

    @Override
    public List<Client> list() {
        return clientRepository.selectAll();
    }

    public List<Client> listeClientsDispo() {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getUser() == null)
                .toList();
    }

    public boolean numDispo(String tel) {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getTelephone() == tel)
                .findFirst()
                .isPresent();
    }

    public Client findClient(String telephone) {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getTelephone().compareTo(telephone) == 0)
                .findFirst()
                .orElse(null);
    }

}
