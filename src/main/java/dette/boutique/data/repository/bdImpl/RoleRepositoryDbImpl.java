package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.impl.RepositoryDbImpl;
import dette.boutique.data.entities.Role;
import dette.boutique.data.repository.RoleRepository;

public class RoleRepositoryDbImpl extends RepositoryDbImpl<Role> implements RoleRepository {

    RoleRepository roleRepository;

    public RoleRepositoryDbImpl(RoleRepository roleRepository) {
        this.tableName = "role";
        this.roleRepository = roleRepository;
    }

    private final String INSERT_QUERY = "INSERT INTO role (nom) VALUES (?)";
    private static final String SELECT_QUERY = "SELECT role.id AS role_id, role.nom AS role_nom FROM user";

    @Override
    public void insert(Role role) {
        try {
            this.connexion();
            this.init(INSERT_QUERY);
            setFields(this.ps, role);
            this.executeUpdate();
            ResultSet rs = this.ps.getGeneratedKeys();
            if (rs.next()) {
                role.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du role : " + e.getMessage());
        } finally {
            try {
                this.deconnexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Role> selectAll() {
        List<Role> listRoles = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connexion();
            this.init(SELECT_QUERY);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                listRoles.add(convertToObject(resultSet));
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

        return listRoles;
    }

    @Override
    public void setFields(PreparedStatement pstmt, Role element) throws SQLException {
        pstmt.setString(2, element.getNom());
    }

    @Override
    public String generateSql(Role element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Role convertToObject(ResultSet rs) throws SQLException {
        String nom = rs.getString("role_nom");
        int id = rs.getInt("role_id");
        return new Role(id, nom);
    }

}
