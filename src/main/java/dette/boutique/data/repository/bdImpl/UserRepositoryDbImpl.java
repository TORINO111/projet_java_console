package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.DataBaseImpl;
import dette.boutique.core.database.impl.RepositoryDbImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryDbImpl extends RepositoryDbImpl<User> implements UserRepository {

    private static final String INSERT_QUERY = "INSERT INTO user (nom, prenom, login, password, client_id, role_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_WITHOUT_USER_QUERY = "INSERT INTO user (login, password, role_id) VALUES (?, ?, ?)";
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
    public boolean insertWithoutClient(User user) {
        try {
            connexion();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_WITHOUT_USER_QUERY);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getRole().ordinal());
            return executeUpdate(pstmt.toString());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'utilisateur sans client : " + e.getMessage());
            return false;
        } finally {
            deconnexion();
        }
    }

    @Override
    public boolean updateClientForUser(User user) {
        try {
            connexion();
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_CLIENT_USER);
            pstmt.setInt(1, user.getClient() != null ? user.getClient().getId() : null);
            pstmt.setString(2, user.getNom());
            pstmt.setString(3, user.getPrenom());
            pstmt.setInt(4, user.getId());
            return executeUpdate(pstmt.toString());
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            return false;
        } finally {
            deconnexion();
        }
    }

    @Override
    public List<User> selectAll() {
        List<User> listUsers = new ArrayList<>();
        try {
            connexion();
            PreparedStatement pstmt = connection.prepareStatement(SELECT_QUERY);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                listUsers.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la BD : " + e.getMessage());
        } finally {
            deconnexion();
        }
        return listUsers;
    }

    @Override
    public User convertToObject(ResultSet rs) throws SQLException {
        
        return null;
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

    @Override
    public void setFields(PreparedStatement pstmt, User element) throws SQLException {
        pstmt.setString(1, element.getNom());
        pstmt.setString(2, element.getPrenom());
        pstmt.setString(3, element.getLogin());
        pstmt.setString(4, element.getPassword());

        if (element.getClient() != null) {
            pstmt.setInt(5, element.getClient().getId());
        } else {
            pstmt.setNull(5, java.sql.Types.INTEGER);
        }
        pstmt.setInt(6, element.getRole().ordinal());
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String nomUser = resultSet.getString("user_nom");
        String prenomUser = resultSet.getString("user_prenom");
        String loginUser = resultSet.getString("user_login");
        String passwordUser = resultSet.getString("user_password");
        String roleNom = resultSet.getString("role_nom");

        Role role = roleNom != null ? Role.valueOf(roleNom) : null;

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
    public User selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }
}