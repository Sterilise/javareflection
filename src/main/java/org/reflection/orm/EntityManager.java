package org.reflection.orm;

import org.reflection.Person;

public interface EntityManager<T> {
    static <T> EntityManager<T> of(Class<T> clss) {

        return new EntityManagerImplementation<>();
    }

    void persist(T t);
}
