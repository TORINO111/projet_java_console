package dette.boutique.data.entities;

import java.util.List;

import lombok.Data;

@Data
public class Dette {
    private int id;
    private int newDette = 0;
    private String date;
    private int montant;
    private int montantVerse;
    private int montantRestant;
    private Client client;
    private List<Details> details;

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
