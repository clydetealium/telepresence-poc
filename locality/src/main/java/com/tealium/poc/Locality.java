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
@Table(name = "locality")
@NamedQuery(name = "Locality.findAll", query = "SELECT l FROM Locality l ORDER BY l.name", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class Locality {

    @Id
    @SequenceGenerator(name = "localitySequence", sequenceName = "locality_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "localitySequence")
    private Integer id;

    @Column(length = 40, unique = true)
    private String name;

    @Column(length = 3)
    private int temperature;

    public Locality() {
    }

    public Locality(String name, int temperature) {
        this.name = name;
        this.temperature = temperature;
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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

}
