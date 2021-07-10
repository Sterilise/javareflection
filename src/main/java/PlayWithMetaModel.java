import org.reflection.Person;
import org.reflection.util.ColumnField;
import org.reflection.util.MetaModel;
import org.reflection.util.PrimaryKeyField;

import java.util.List;

public class PlayWithMetaModel {
    public static void main(String[] args){
        //MetaModel class retrieves the primary keys and column fields of a given class
        MetaModel<Person> metamodel = MetaModel.of(Person.class);

        PrimaryKeyField primaryKeyField = metamodel.getPrimaryKey();
        List<ColumnField> columnFields =  metamodel.getColumns();

        System.out.println("Primary key name = " + primaryKeyField.getName() + ", type = " + primaryKeyField.getTypeName());


        for (ColumnField columnField : columnFields) {
            System.out.println("Column name = " + columnField.getName() + ", type = " + columnField.getTypeName());
        }

    }
}
