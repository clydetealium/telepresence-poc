package com.tealium.poc.client.locality;

public class Locality {

    private Integer id;
    private String name;
    private int temperature;

    public Locality(){}

    public Locality(String name, int temperature) {
        this.name = name;
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public int getTemperature() {
        return temperature;
    }
}
