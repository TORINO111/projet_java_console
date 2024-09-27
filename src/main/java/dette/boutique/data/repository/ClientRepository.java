package dette.boutique.data.repository;

import dette.boutique.core.database.Repository;
import dette.boutique.data.entities.Client;

public interface ClientRepository extends Repository<Client> {
    Client findByTel(String telephone);

    void updateUserForClient(Client client);

}
