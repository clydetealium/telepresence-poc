package com.tealium.poc.client.personality;

public class Personality {

    private Integer id;
    private String name;
    private int threshold;
    private String locality;

    public Personality(){}

    public Personality(String name, int threshold, String locality) {
        this.name = name;
        this.threshold = threshold;
        this.locality = locality;
    }

    public String getName() {
        return name;
    }

    public int getThreshold() {
        return threshold;
    }

    public String getLocality() {
        return locality;
    }
}
