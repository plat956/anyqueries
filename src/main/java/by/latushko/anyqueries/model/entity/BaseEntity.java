package by.latushko.anyqueries.model.entity;

import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable, Cloneable {
    private T id;
    private transient long total;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}


