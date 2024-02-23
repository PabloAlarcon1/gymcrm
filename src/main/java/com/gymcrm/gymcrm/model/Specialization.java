package com.gymcrm.gymcrm.model;


import jakarta.persistence.*;

@Entity
@Table(name = "SPECIALIZATION")
public class Specialization {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;


    @Column(name = "NAME", nullable = false)
    private String name;




    // Constructores

    public Specialization() {
    }

    public Specialization(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Specialization(String name) {
        this.name = name;
    }




    // Getters y Setters

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
}
