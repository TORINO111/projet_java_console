package dette.boutique.data.entities;

import java.util.List;

import lombok.Data;

@Data
public class Article {
    private int id;
    private int increment=0;
    private String ref;
    private String libelle;
    private int prixUnitaire;
    private int qteStock;
    private List<Details> details;

    public Article() {
        this.id +=increment;
        this.ref = String.format("A%06d", id);

    }

    public Article(String lib, int price, int stock) {
        this.id +=increment;
        this.ref = String.format("A%06d", id);
        this.libelle = lib;
        this.prixUnitaire = price;
        this.qteStock = stock;
    }

    @Override
    public String toString() {
        return "Article[ref=" + ref + "; libelle=" + libelle + "; prix unitaire=" + prixUnitaire + "; stock= "
                + qteStock
                + "]";
    }
}
