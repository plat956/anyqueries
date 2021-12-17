package by.latushko.anyqueries.entity;

import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable, Cloneable {
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}


