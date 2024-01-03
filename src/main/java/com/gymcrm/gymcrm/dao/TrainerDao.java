package com.gymcrm.gymcrm.dao;

import com.gymcrm.gymcrm.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerDao {
    Trainer save(Trainer trainer);
    Trainer findById(Long id);
    List<Trainer> findAll();
    Trainer update(Trainer trainer);
    void delete(Long id);

}
