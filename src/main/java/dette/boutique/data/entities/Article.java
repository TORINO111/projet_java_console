package dette.boutique.data.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 55, nullable = false)
    private String ref;

    @Column(length = 55, unique = true, nullable = false)
    private String libelle;

    @Column(nullable = false)
    private int qteStock;

    @Column(nullable = false)
    private int prixUnitaire;

    @OneToMany(mappedBy = "article")
    private List<Details> details;

    @Transient
    private static int increment = 0;

    public Article() {
        this.id += increment;
        this.ref = String.format("A%06d", id);
    }

    public Article(String lib, int price, int stock) {
        this.id += increment;
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
