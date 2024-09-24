package dette.boutique.core.factory;

import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.data.repository.listImpl.ClientRepositoryListImpl;
import dette.boutique.data.repository.listImpl.UserRepositoryListImpl;

public class Factory {
    private static ClientRepository clientRepository;
    private static UserRepository userRepository;

    public static ClientRepository getinstanceClientRepository() {
        if (clientRepository == null) {
            clientRepository = new ClientRepositoryListImpl();
        }
        return clientRepository;
    }

    public static UserRepository getinstanceUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepositoryListImpl();
        }
        return userRepository;
    }
}
