package com.design.tonic.tonicjsontest.model;

/**
 * Created by gjacobs on 9/14/15.
 *
 * Person object found in people list converted from people.json
 */
public class Person {
    private String first_name;
    private String last_name;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
