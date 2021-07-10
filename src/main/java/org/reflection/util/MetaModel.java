package org.reflection.util;

import org.reflection.Person;
import org.reflection.annotation.Column;
import org.reflection.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MetaModel{


    private final Class<?> clss;

    public static MetaModel of(Class<?> clss){
        return new MetaModel(clss);
    }

    public MetaModel(Class<?> clss){
        this.clss = clss;
    }

    public PrimaryKeyField getPrimaryKey(){

        //look through all fields in the class
        Field[] fields = this.clss.getDeclaredFields();

        //find the primary key field in that class
        for (Field field: fields){
            PrimaryKey fieldAnnotation = field.getAnnotation(PrimaryKey.class);
            if (fieldAnnotation != null){
                return new PrimaryKeyField(field);
            }
        }
        throw new IllegalArgumentException("Lack of Primary Field Annotation in the specified class:\t" + clss.getSimpleName());
    }

    public List<ColumnField> getColumns() {
        //look through all fields in the class
        Field[] fields = this.clss.getDeclaredFields();

        //find all the fields marked with column in the specified class
        List<ColumnField> columnFields = new ArrayList<>();

        for (Field field: fields){
            Column fieldAnnotation = field.getAnnotation(Column.class);
            if (fieldAnnotation != null){
                columnFields.add(new ColumnField(field));
            }
        }
        return columnFields;
    }

    public String buildInsertRequest() {
        //insert into person (id, name, age) values (?, ?, ?)
        String primaryKeyColumnName = getPrimaryKey().getName();
        List<String> columnNames = getColumns().stream().map(ColumnField::getName).collect(Collectors.toList());


        List<String> allNames = columnNames;
        allNames.add(0, primaryKeyColumnName);
        //join elements in list using , as a separator
        String tableFieldsPart = String.join(", ", allNames);
        int numberOfTableFields = allNames.size();
        String questionMarkPart = IntStream.range(0, numberOfTableFields).mapToObj(index -> "?").collect(Collectors.joining(", "));

        return "insert into " + this.clss.getSimpleName() + " (" + tableFieldsPart + ") values (" + questionMarkPart + ")";
    }

    public String buildSelectRequest() {
        //select id, name, age from Person where id = ?
        String primaryKeyColumnName = getPrimaryKey().getName();
        List<String> columnNames = getColumns().stream().map(ColumnField::getName).collect(Collectors.toList());


        List<String> allNames = columnNames;
        allNames.add(0, primaryKeyColumnName);
        //join elements in list using , as a separator
        String tableFieldsPart = String.join(", ", allNames);


        return "SELECT " + tableFieldsPart + " FROM " + this.clss.getSimpleName() + " where " + getPrimaryKey().getName() + " ?";
    }
}
