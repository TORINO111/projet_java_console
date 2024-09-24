package dette.boutique.data.entities;

import java.util.List;

import lombok.Data;;

@Data
public class Client {
    private int id;
    private int increment = 0;
    private String prenom;
    private String nom;
    private String adresse;
    private String telephone;
    private User user;
    private List<Dette> dettes;

    public Client() {
        this.user = null;
        this.id += increment;
    }

    public Client(String nom, String prenom, String adresse, String telephone) {
        this.id = ++increment;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.user = null;
    }

    public Client(String nom, String prenom, String adresse, String telephone, User user) {
        this.id = ++increment;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.user = user;
    }

    public Client(int id, String nom, String prenom, String telephone, String adresse, User user) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Client[Nom=" + nom + ", Prénom=" + prenom + ", Adresse=" + adresse + ", Telephone=" + telephone + "]";
    }
}
