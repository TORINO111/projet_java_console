package dette.boutique.services;

import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.ArticleRepository;

public class ArticleService implements Item<Article> {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void create(Article article) {
        articleRepository.insert(article);
    }

    @Override
    public List<Article> list() {
        return articleRepository.selectAll();
    }

    public List<Article> listeArticlesDispo() {
        return list().stream()
                .filter(article -> article.getQteStock() != 0)
                .toList();
    }

    public List<Article> isDispo(boolean choix) {
        if (choix) {
            return list().stream()
                    .filter(article -> article.getQteStock() != 0)
                    .toList();
        } else {
            return list().stream()
                    .filter(article -> article.getQteStock() == 0)
                    .toList();
        }
    }

    public Article findArticle(String libelle) {
        return list().stream()
                .filter(article -> article.getLibelle().compareTo(libelle) == 0)
                .findFirst()
                .orElse(null);
    }

    public boolean changeStock(int quantite, Article article) {
        article.setQteStock(quantite);
        return true;
    }

}
