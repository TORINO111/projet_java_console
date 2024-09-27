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
import lombok.Data;

@Data
@Entity
@Table(name = "dette")
public class Dette {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "dette")
    private List<Details> details;

    @Column(nullable = false)
    private int montantVerse;

    @Column(nullable = false)
    private int montant;

    @Column(nullable = false)
    private int montantRestant;

    @Transient
    private static int newDette = 0;

    @Column(nullable = false)
    private String date;

    public Dette() {
        this.id = ++newDette;
    }

    public Dette(String date, int montant, int montantVerse, Client client, List<Details> details) {
        this.id = ++newDette;
        this.date = date;
        this.montant = montant;
        this.montantVerse = montantVerse;
        this.montantRestant = montant - montantVerse;
        this.client = client;
        this.details = details;
    }

    @Override
    public String toString() {
        return "Client: " + client.getNom() + " " + client.getPrenom() + "; montant:" + montant + "; montant versé:"
                + montantVerse + "; montant restant:" + montantRestant;
    }
}
