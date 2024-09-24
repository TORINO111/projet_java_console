package dette.boutique.data.repository;

import dette.boutique.core.database.Repository;
import dette.boutique.data.entities.Client;

public interface ClientRepository extends Repository<Client> {
    Client findByTel(String telephone);

    boolean insertWithoutUser(Client client);

    boolean updateUserForClient(Client client);

}
