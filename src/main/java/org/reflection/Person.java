package org.reflection;

import org.reflection.annotation.Column;
import org.reflection.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Just a regular person bean, to be written and read from storage using reflection api
 */
public class Person implements Serializable{
    @PrimaryKey
    private long id;

    @Column
    private String name;

    @Column
    private int age;

    public Person(){
    }

    public Person(String name, int age){
        this.name = name;
        this.age = age;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



}
