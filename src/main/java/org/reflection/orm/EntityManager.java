package org.reflection.orm;

import org.reflection.Person;

import java.sql.SQLException;

public interface EntityManager<T> {
    static <T> EntityManager<T> of(Class<T> clss) {

        return new EntityManagerImplementation<>();
    }

    void persist(T t) throws SQLException, IllegalAccessException;
}
