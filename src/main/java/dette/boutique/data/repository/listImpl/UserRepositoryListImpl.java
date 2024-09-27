package dette.boutique.data.repository.listImpl;

import dette.boutique.core.database.impl.RepositoryListImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryListImpl extends RepositoryListImpl<User> implements UserRepository {

    @Override
    public void updateClientForUser(User user) {
        user.setClient(user.getClient());
        user.setNom(user.getClient().getNom());
        user.setPrenom(user.getClient().getPrenom());
    }

    @Override
    public User selectByLogin(String login) {
        throw new UnsupportedOperationException("Unimplemented method 'selectByLogin'");
    }

    @Override
    public User selectById(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

}
