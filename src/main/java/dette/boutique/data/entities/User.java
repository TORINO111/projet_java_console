package dette.boutique.data.entities;

import dette.boutique.data.enums.Role;
import lombok.Data;

@Data
public class User {
    private int id;
    private int increment = 0;
    private String login;
    private String nom;
    private String prenom;
    private String password;
    private Client client;
    private boolean active;
    private Role role;

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
