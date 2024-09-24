package dette.boutique.data.repository.bdImpl;

import java.util.List;

import dette.boutique.core.database.impl.RepositoryDbImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.ArticleRepository;

public class ArticleRepositoryDbImpl extends RepositoryDbImpl<Article> implements ArticleRepository {

    @Override
    public boolean insert(Article element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public List<Article> selectAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
    }

    @Override
    public Article findByLibelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByLibelle'");
    }

}
