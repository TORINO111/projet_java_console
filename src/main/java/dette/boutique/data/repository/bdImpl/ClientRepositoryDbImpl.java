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
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.UserRepository;

public class ClientRepositoryDbImpl extends RepositoryDbImpl<Client> implements ClientRepository {
    UserRepository userRepository;

    public ClientRepositoryDbImpl(UserRepository userRepository) {
        this.tableName = "client";
        this.userRepository = userRepository;
    }

    private final String INSERT_QUERY ="INSERT INTO client (nom, prenom, telephone, adresse, user_id) VALUES (?, ?, ?, ?, ?)";
    // private final String INSERT_WITHOUT_USER_QUERY = String
    // .format("INSERT INTO %S (nom, prenom, telephone, adresse) VALUES (?, ?, ?,
    // ?)", tableName);
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
    public List<Client> selectAll() {
        List<Client> listClients = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listClients.add(convertToObject(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
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

        return listClients;
    }

    @Override
    public Client findByTel(String telephone) {
        Client client = null;
        try {
            connexion();
            this.init(SELECT_CLIENT_QUERY);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                client = convertToObject(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        } finally {
            try {
                deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    @Override
    public void updateUserForClient(Client client) {
        try {
            connexion();
            this.init(UPDATE_USER_CLIENT);
            ps.setInt(1, client.getUser().getId());
            ps.setInt(2, client.getId());
            this.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur du client : " + e.getMessage());
        } finally {
            try {
                deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    @Override
    public String generateSql(Client element) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Client convertToObject(ResultSet resultSet) throws SQLException {
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
                    Role.findRoleByName(resultSet.getString("role_nom")));
        }


        return new Client(clientId, nomClient, prenomClient, telephone, adresse, user);    }
}
