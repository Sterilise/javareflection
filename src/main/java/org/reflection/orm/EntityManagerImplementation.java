package org.reflection.orm;

import org.reflection.util.ColumnField;
import org.reflection.util.MetaModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public class EntityManagerImplementation<T> implements EntityManager<T> {
    private AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {
        MetaModel metaModel = MetaModel.of(t.getClass());
        String sql = metaModel.buildInsertRequest();
        PreparedStatement statement = prepareStatementWith(sql).andParameters(t);
        statement.executeUpdate();
    }

    @Override
    public T find(Class<T> clss, Object primaryKey) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        MetaModel metamodel = MetaModel.of(clss);
        String sql = metamodel.buildSelectRequest();
        PreparedStatement statement = prepareStatementWith(sql).andPrimaryKey(primaryKey);
        ResultSet resultSet = statement.executeQuery();

        return buildInstanceFrom(clss, resultSet);
    }

    private T buildInstanceFrom(Class<T> clss, ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        MetaModel metamodel = MetaModel.of(clss);

        //create a new instance of the class
        T t = clss.getConstructor().newInstance();

        //set primary key of object
        Field primary = metamodel.getPrimaryKey().getField();
        String primaryKeyColumnName = metamodel.getPrimaryKey().getName();
        Class<?> primaryKeyType = metamodel.getPrimaryKey().getType();


        resultSet.next();

        if(primaryKeyType == long.class){
            long primaryKey = resultSet.getInt(primaryKeyColumnName);
            primary.setAccessible(true);
            primary.set(t, primaryKey);
        }

        for(ColumnField columnField: metamodel.getColumns()){
            Field field = columnField.getField();
            field.setAccessible(true);

            Class<?> columnType = columnField.getType();
            String columnName = columnField.getName();

            if(columnType == int.class){
                int value = resultSet.getInt(columnName);
                field.set(t, value);
            } else if (columnType == String.class){
                String value = resultSet.getString(columnName);
                field.set(t, value);
            }
        }

        return t;


    }

    private PreparedStatementWrapper prepareStatementWith(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:C:\\Users\\mea\\IdeaProjects\\reflection\\db-files\\db", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sql);
        
        return new PreparedStatementWrapper(statement);
    }

    /**
     * adds content to the prepared statement before it is executed.
     */

    private class PreparedStatementWrapper{

        private final PreparedStatement statement;

        public PreparedStatementWrapper(PreparedStatement statement) {
            this.statement = statement;
        }

        public PreparedStatement andParameters(T t) throws SQLException, IllegalAccessException {
            MetaModel metamodel = MetaModel.of(t.getClass());
            Class<?> primaryKeyType = metamodel.getPrimaryKey().getType();

            //primary key (id)
            if(primaryKeyType == long.class){
                long id = idGenerator.incrementAndGet();
                statement.setLong(1, id);
                Field primaryKeyField = metamodel.getPrimaryKey().getField();
                primaryKeyField.setAccessible(true);
                primaryKeyField.set(t, id);
            }

            //columns
            for(int columnIndex = 0; columnIndex < metamodel.getColumns().size(); columnIndex++){
                ColumnField columnField = metamodel.getColumns().get(columnIndex);
                Class<?> fieldType = columnField.getType();

                //read field using reflection api
                Field field = columnField.getField();
                field.setAccessible(true);
                Object value = field.get(t);

                //iterate through the different types of fields

                if( fieldType == int.class){
                    //column index starts at parameter 2
                    statement.setInt(columnIndex + 2, (int) value);
                } else if(fieldType == String.class){
                    statement.setString(columnIndex + 2, (String) value);
                }
            }
            return statement;
        }

        public PreparedStatement andPrimaryKey(Object primaryKey) throws SQLException {
            if (primaryKey.getClass() == Long.class){
                statement.setLong(1, (Long) primaryKey);
            }
            return statement;
        }
    }
}
