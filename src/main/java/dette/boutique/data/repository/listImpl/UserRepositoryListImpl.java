package dette.boutique.data.repository.listImpl;

import dette.boutique.core.database.impl.RepositoryListImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryListImpl extends RepositoryListImpl<User> implements UserRepository {

    @Override
    public boolean insertWithoutClient(User user) {
        data.add(user);
        return true;
    }

    @Override
    public boolean updateClientForUser(User user) {
        user.setClient(user.getClient());
        user.setNom(user.getClient().getNom());
        user.setPrenom(user.getClient().getPrenom());
        return true;
    }

    @Override
    public User selectByLogin(String login) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectByLogin'");
    }

}
