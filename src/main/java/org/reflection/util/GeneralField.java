package org.reflection.util;

import java.lang.reflect.Field;

public class GeneralField {


    private Field field;

    public GeneralField(Field field){
        this.field = field;
    }
    public String getName() {
        return this.field.getName();
    }

    public String getTypeName(){
        return this.field.getType().getSimpleName();
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
