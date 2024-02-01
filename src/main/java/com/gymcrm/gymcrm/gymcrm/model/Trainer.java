package com.gymcrm.gymcrm.gymcrm.model;

import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings;


    // Constructores

    public Trainer() {
    }

    public Trainer(Long id, Specialization specialization, User user, List<Training> trainings) {
        this.id = id;
        this.specialization = specialization;
        this.user = user;
        this.trainings = trainings;
    }

    public Trainer(Specialization specialization, User user, List<Training> trainings) {
        this.specialization = specialization;
        this.user = user;
        this.trainings = trainings;
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

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}



