package dette.boutique.core.factory;

import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.data.repository.jpaImpl.ClientRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.UserRepositoryJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Factory {
    private static ClientRepository clientRepository;
    private static UserRepository userRepository;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySqlBi");

    public static ClientRepository getinstanceClientRepository(UserRepository userRepository) {
        if (clientRepository == null) {
            EntityManager em = emf.createEntityManager();
            clientRepository = new ClientRepositoryJpaImpl(em, Client.class, userRepository);
            // clientRepository = new ClientRepositoryDbImpl(userRepository)
        }
        return clientRepository;
    }

    public static UserRepository getinstanceUserRepository() {
        if (userRepository == null) {
            EntityManager em = emf.createEntityManager();
            userRepository = new UserRepositoryJpaImpl(em, User.class);
        }
        return userRepository;
    }

}
