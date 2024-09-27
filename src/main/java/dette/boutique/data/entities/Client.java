package dette.boutique.data.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 25, unique = true, nullable = false)
    private String telephone;

    @Column(length = 255, nullable = false)
    private String adresse;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client")
    private List<Dette> dettes;

    @Transient()
    private int increment = 0;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

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
