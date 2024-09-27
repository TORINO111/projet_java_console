package dette.boutique.data.entities;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 25, unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String nom;

    @ColumnDefault(value = "true")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    @Transient
    private int increment = 0;


    public User() {
        this.client = null;
        this.active = false;
        this.id += increment;
    }

    public User(String login, String passsword, Role role) {
        this.id += increment;
        this.login = login;
        this.password = passsword;
        this.role = role;
        this.active = false;
    }

    public User(String nom, String prenom, String login, String password, Client client, Role role) {
        this.id += increment;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.login = login;
        this.client = client;
        this.role = role;
        this.active = true;
    }

    public User(int id, String nom, String prenom, String login, String password, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.login = login;
        this.role = role;
        this.active = true;
    }

    @Override
    public String toString() {
        return "User[login=" + login + ", nom=" + nom + ", prenom=" + prenom + ", active=" + active + ", role=" + role
                + "]";
    }
}
