package dette.boutique.data.repository.listImpl;

import dette.boutique.core.database.impl.RepositoryListImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.ArticleRepository;

public class ArticleRepositoryListImpl extends RepositoryListImpl<Article> implements ArticleRepository {

    @Override
    public Article findByLibelle(String libelle) {
        for (Article article : data) {
            if (article.getLibelle().equals(libelle)) {
                return article;
            }
        }
        return null;
    }
}
