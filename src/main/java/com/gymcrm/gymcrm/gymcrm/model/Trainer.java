package com.gymcrm.gymcrm.gymcrm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TRAINER")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SPECIALIZATION_ID")
    private Specialization specialization;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;




    // Constructores

    public Trainer() {
    }

    public Trainer(Long id, Specialization specialization, User user) {
        this.id = id;
        this.specialization = specialization;
        this.user = user;
    }

    public Trainer(Specialization specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }




    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
