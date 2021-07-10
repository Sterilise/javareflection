package org.reflection.util;

import org.reflection.Person;
import org.reflection.annotation.Column;
import org.reflection.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class MetaModel<T> {


    private final Class<T> clss;

    public static <T> MetaModel<T> of(Class<T> clss){
        return new MetaModel(clss);
    }

    public MetaModel(Class<T> clss){
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
}
