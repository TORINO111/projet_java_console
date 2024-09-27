package dette.boutique.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "details_dette_article")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private int prixTotal;

    @ManyToOne()
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne()
    @JoinColumn(name = "dette_id")
    private Dette dette;

    public Details(Article article, int quantité) {
        this.article = article;
        this.quantite = quantité;
        this.prixTotal = article.getPrixUnitaire() * quantité;
    }
}
