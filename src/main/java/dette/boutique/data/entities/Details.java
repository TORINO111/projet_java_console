package dette.boutique.data.entities;

import lombok.Data;

@Data
public class Details {
    private int quantité;
    private int prixTotal;
    private Article article;
    private Dette dette;

    public Details(Article article, int quantité) {
        this.article = article;
        this.quantité = quantité;
        this.prixTotal = article.getPrixUnitaire() * quantité;
    }
}
