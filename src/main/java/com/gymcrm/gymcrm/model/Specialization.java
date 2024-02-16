package com.gymcrm.gymcrm.model;


import javax.persistence.*;

@Entity
@Table(name = "SPECIALIZATION")
public class Specialization {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @Column(name = "NAME", nullable = false)
    private String name;




    // Constructores

    public Specialization() {
    }

    public Specialization(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Specialization(String name) {
        this.name = name;
    }




    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
