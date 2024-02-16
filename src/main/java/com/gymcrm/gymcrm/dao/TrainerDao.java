package com.gymcrm.gymcrm.dao;

import com.gymcrm.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerDao {
    Trainer save(Trainer trainer);
    Trainer findById(Long id);
    Trainer findByUserName(String userName);
    List<Trainer> findAll();
    Trainer update(Trainer trainer);
    void delete(Long id);
    boolean existsByUsername(String userName);

}
