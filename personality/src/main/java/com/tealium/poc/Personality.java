package com.tealium.poc;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "personality")
@NamedQuery(name = "Personality.findAll", query = "SELECT p FROM Personality p ORDER BY p.name", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class Personality {

    @Id
    @SequenceGenerator(name = "personalitySequence", sequenceName = "personality_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "personalitySequence")
    private Integer id;

    @Column(length = 40, unique = true)
    private String name;

    @Column(length = 3)
    private int threshold;

    @Column(length = 40)
    private String locality;

    public Personality() {
    }

    public Personality(String name, String locality, int threshold) {
        this.name = name;
        this.locality = locality;
        this.threshold = threshold;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

}
