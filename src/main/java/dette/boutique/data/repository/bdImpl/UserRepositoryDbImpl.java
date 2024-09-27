package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.impl.RepositoryDbImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryDbImpl extends RepositoryDbImpl<User> implements UserRepository {

    private static final String INSERT_QUERY = "INSERT INTO user (nom, prenom, login, password, client_id, role_id) VALUES (?, ?, ?, ?, ?, ?)";
    // private static final String INSERT_WITHOUT_USER_QUERY = "INSERT INTO user
    // (login, password, role_id) VALUES (?, ?, ?)";
    private static final String SELECT_QUERY = "SELECT user.id AS user_id, user.nom AS user_nom, user.prenom AS user_prenom, "
            + "user.login AS user_login, user.password AS user_password, "
            + "role.nom AS role_nom, "
            + "client.id AS client_id, client.nom AS client_nom, client.prenom AS client_prenom, "
            + "client.telephone AS client_telephone, client.adresse AS client_adresse "
            + "FROM user "
            + "LEFT JOIN client ON user.id = client.user_id "
            + "LEFT JOIN role ON user.role_id = role.id";
    private static final String UPDATE_CLIENT_USER = "UPDATE user "
            + "SET user.client_id = ?, "
            + "user.nom = ?, "
            + "user.prenom = ? "
            + "WHERE user.id = ?";

    @Override
    public void insert(User user) {
        try {
            this.connexion();
            this.init(INSERT_QUERY);
            setFields(this.ps, user);
            this.executeUpdate();
            ResultSet rs = this.ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'utilisateur : " + e.getMessage());
        } finally {
            try {
                this.deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateClientForUser(User user) {
        try {
            connexion();
            this.init(UPDATE_CLIENT_USER);
            ps.setInt(1, user.getClient() != null ? user.getClient().getId() : null);
            ps.setString(2, user.getClient().getNom());
            ps.setString(3, user.getClient().getPrenom());
            ps.setInt(4, user.getId());
            this.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        } finally {
            try {
                deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> selectAll() {
        List<User> listUsers = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listUsers.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la BD : " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
            try {
                deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listUsers;
    }

    @Override
    public User convertToObject(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String nomUser = resultSet.getString("user_nom");
        String prenomUser = resultSet.getString("user_prenom");
        String loginUser = resultSet.getString("user_login");
        String passwordUser = resultSet.getString("user_password");
        String roleNom = resultSet.getString("role_nom");

        Role role = roleNom != null ? Role.findRoleByName(roleNom) : null;

        User user = new User(userId, nomUser, prenomUser, loginUser, passwordUser, role);

        int clientId = resultSet.getInt("client_id");
        if (!resultSet.wasNull()) {
            String nomClient = resultSet.getString("client_nom");
            String prenomClient = resultSet.getString("client_prenom");
            String telephoneClient = resultSet.getString("client_telephone");
            String adresseClient = resultSet.getString("client_adresse");

            Client client = new Client(clientId, nomClient, prenomClient, telephoneClient, adresseClient, user);
            user.setClient(client);
        }

        return user;
    }

    @Override
    public void setFields(PreparedStatement ps, User element) throws SQLException {
        if (element.getNom() != null) {
            ps.setString(1, element.getNom());
        } else {
            ps.setNull(1, java.sql.Types.VARCHAR);
        }
    
        if (element.getPrenom() != null) {
            ps.setString(2, element.getPrenom());
        } else {
            ps.setNull(2, java.sql.Types.VARCHAR);
        }
    
        ps.setString(3, element.getLogin());
        ps.setString(4, element.getPassword());
    
        if (element.getClient() != null) {
            ps.setInt(5, element.getClient().getId());
        } else {
            ps.setNull(5, java.sql.Types.INTEGER);
        }
    
        ps.setInt(6, element.getRole().getId());
    }
    
    @Override
    public User selectById(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public User selectByLogin(String login) {
        // Méthode non implémentée
        throw new UnsupportedOperationException("Unimplemented method 'selectByLogin'");
    }

    @Override
    public String generateSql(User element) {
        // Méthode non implémentée
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

}