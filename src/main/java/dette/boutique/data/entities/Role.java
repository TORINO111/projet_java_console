package dette.boutique.data.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role {
    private static Connection connection;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String nom;

    public Role() {
    }

    public Role(String nom) {
        this.nom = nom;
    }

    public Role(int id, String name) {
        this.id = id;
        this.nom = name;
    }

    public static List<Role> listerRoles(EntityManager em) {
        return em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    public static List<Role> listerRolesExceptClient(EntityManager em) {
        return listerRoles(em).stream()
                .filter(role -> !role.getNom().equals("CLIENT"))
                .toList();
    }

    public static Role findRoleByName(String roleNom) throws SQLException {
        String sql = "SELECT * FROM role WHERE nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roleNom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    return new Role(id, roleNom);
                } else {
                    return null;
                }
            }
        }
    }

    
}
