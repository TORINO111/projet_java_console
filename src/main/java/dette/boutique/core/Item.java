package dette.boutique.core;

import java.util.List;

public interface Item<T> {
    public void create(T element);

    public List<T> list();
}
