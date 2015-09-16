package com.design.tonic.tonicjsontest.model;

import java.util.List;

/**
 * List of people found in people.json
 */
public class People {
    private List<Person> people;
    public List<Person> getPeople() {
        return people;
    }
    public void setPeople(List<Person> people) {
        this.people = people;
    }
}
