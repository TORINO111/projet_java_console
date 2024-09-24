package dette.boutique.data.repository;

import dette.boutique.core.database.Repository;
import dette.boutique.data.entities.User;

public interface UserRepository extends Repository<User> {
    boolean insertWithoutClient(User user);

    boolean updateClientForUser(User user);

    User selectByLogin(String login);

    User selectById(int id);
}
