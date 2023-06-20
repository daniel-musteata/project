package org.example.models;

public class Person {

    private int id;
    private String name;
    private String email;


    public Person() {}

    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
