package dette.boutique.core.database.impl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.Repository;

public abstract class RepositoryListImpl<T> implements Repository<T> {
    protected List<T> data = new ArrayList<>();

    @Override
    public void insert(T element) {
        data.add(element);
    }

    @Override
    public List<T> selectAll() {
        return new ArrayList<>(data);
    }
}