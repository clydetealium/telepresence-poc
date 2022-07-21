package com.tealium.poc;

import java.util.Objects;

public class Info {
    private String person;
    private Mood mood;

    public Info(String person, Mood mood) {
        this.person = person;
        this.mood = mood;
    }

    public String getPerson() {
        return this.person;
    }

    public Mood getMood() {
        return this.mood;
    }

    @Override
    public String toString() {
        return "Info{" +
                "person='" + person + '\'' +
                ", mood=" + mood +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Info info = (Info) o;
        return Objects.equals(person, info.person) && mood == info.mood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, mood);
    }
}
