package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.impl.RepositoryDbImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.UserRepository;

public class ClientRepositoryDbImpl extends RepositoryDbImpl<Client> implements ClientRepository {
    UserRepository userRepository;

    public ClientRepositoryDbImpl(UserRepository userRepository) {
        this.tableName = "client";
        this.userRepository = userRepository;
    }

    private final String INSERT_QUERY = String
            .format("INSERT INTO %S (nom, prenom, telephone, adresse, user_id) VALUES (?, ?, ?, ?, ?)", tableName);
    private final String INSERT_WITHOUT_USER_QUERY = String
            .format("INSERT INTO %S (nom, prenom, telephone, adresse) VALUES (?, ?, ?, ?)", tableName);
    private static final String SELECT_QUERY = "SELECT client.id AS client_id, client.nom AS client_nom, client.prenom AS client_prenom, "
            + "client.telephone AS client_telephone, client.adresse AS client_adresse, "
            + "user.id AS user_id, user.nom AS user_nom, user.prenom AS user_prenom, "
            + "user.login AS user_login, user.password AS user_password, role.nom AS role_nom "
            + "FROM client "
            + "LEFT JOIN user ON client.user_id = user.id "
            + "LEFT JOIN role ON user.role_id = role.id";
    private static final String SELECT_CLIENT_QUERY = "SELECT client.id AS client_id, client.nom AS client_nom, client.prenom AS client_prenom, "
            + "client.telephone AS client_telephone, client.adresse AS client_adresse, "
            + "user.id AS user_id, user.nom AS user_nom, user.prenom AS user_prenom, "
            + "user.login AS user_login, user.password AS user_password, role.nom AS role_nom "
            + "FROM client "
            + "LEFT JOIN user ON client.user_id = user.id "
            + "LEFT JOIN role ON user.role_id = role.id "
            + "WHERE client.telephone = ?";
    private static final String UPDATE_USER_CLIENT = "UPDATE client "
            + "SET client.user_id = ? "
            + "WHERE client.id = ?";

    @Override
    public void insert(Client client) {
        try {
            this.connexion();
            this.init(INSERT_QUERY);
            setFields(this.ps, client);
            this.executeUpdate();
            ResultSet rs = this.ps.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client : " + e.getMessage());
        } finally {
            try {
                this.deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean insertWithoutUser(Client client) {
        try {
            connexion();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_WITHOUT_USER_QUERY);
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getTelephone());
            pstmt.setString(4, client.getAdresse());
            return executeUpdate(pstmt.toString());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client sans utilisateur : " + e.getMessage());
            return false;
        } finally {
            deconnexion();
        }
    }

    @Override
    public List<Client> selectAll() {
        List<Client> listClients = new ArrayList<>();
        try {
            connexion();
            ResultSet resultSet = executeQuery(SELECT_QUERY);
            if (resultSet != null) {
                while (resultSet.next()) {
                    listClients.add(mapResultSetToClient(resultSet));
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } finally {
            deconnexion();
        }
        return listClients;
    }

    @Override
    public Client findByTel(String telephone) {
        Client client = null;
        try {
            connexion();
            PreparedStatement pstmt = connection.prepareStatement(SELECT_CLIENT_QUERY);
            pstmt.setString(1, telephone);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                client = mapResultSetToClient(resultSet);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        } finally {
            deconnexion();
        }
        return client;
    }

    @Override
    public boolean updateUserForClient(Client client) {
        try {
            connexion();
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_USER_CLIENT);
            pstmt.setInt(1, client.getUser().getId());
            pstmt.setInt(2, client.getId());
            return executeUpdate(pstmt.toString());
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur du client : " + e.getMessage());
            return false;
        } finally {
            deconnexion();
        }
    }

    @Override
    public void setFields(PreparedStatement pstmt, Client client) throws SQLException {
        pstmt.setString(1, client.getNom());
        pstmt.setString(2, client.getPrenom());
        pstmt.setString(3, client.getTelephone());
        pstmt.setString(4, client.getAdresse());
        if (client.getUser() != null) {
            pstmt.setInt(5, client.getUser().getId());
        } else {
            pstmt.setNull(5, java.sql.Types.INTEGER);
        }
    }

    private Client mapResultSetToClient(ResultSet resultSet) throws SQLException {
        int clientId = resultSet.getInt("client_id");
        String nomClient = resultSet.getString("client_nom");
        String prenomClient = resultSet.getString("client_prenom");
        String telephone = resultSet.getString("client_telephone");
        String adresse = resultSet.getString("client_adresse");

        User user = null;
        if (resultSet.getInt("user_id") > 0) {
            user = new User(resultSet.getInt("user_id"),
                    resultSet.getString("user_nom"),
                    resultSet.getString("user_prenom"),
                    resultSet.getString("user_login"),
                    resultSet.getString("user_password"),
                    Role.valueOf(resultSet.getString("role_nom")));
        }

        return new Client(clientId, nomClient, prenomClient, telephone, adresse, user);
    }

    @Override
    public String generateSql(Client element) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Client convertToObject(Client objet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }

    @Override
    public Client convertToObject(ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }
}
