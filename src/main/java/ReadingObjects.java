import org.reflection.Person;
import org.reflection.orm.EntityManager;
import org.reflection.orm.EntityManagerImplementation;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ReadingObjects {

    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person linda = entityManager.find(Person.class, 1);
        Person james = entityManager.find(Person.class, 2);
        Person susan = entityManager.find(Person.class, 3);
        Person john = entityManager.find(Person.class, 4);

    }
}
