import org.reflection.Person;
import org.reflection.orm.EntityManager;

public class WritingObjects {
    public static void main(String[] args){
        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person linda = new Person("Linda", 31);
        Person james = new Person("James", 24);
        Person susan = new Person("Susan", 34);
        Person john = new Person("John", 33);

        entityManager.persist(linda);
        entityManager.persist(james);
        entityManager.persist(susan);
        entityManager.persist(john);
    }
}
