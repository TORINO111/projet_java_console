package dette.boutique.data.repository;

import dette.boutique.core.database.Repository;
import dette.boutique.data.entities.Article;

public interface ArticleRepository extends Repository<Article> {
    public Article findByLibelle(String libelle);

}
