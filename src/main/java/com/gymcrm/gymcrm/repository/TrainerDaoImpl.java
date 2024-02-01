package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDaoImpl implements TrainerDao {

    private final Map<Long, Trainer> trainerMap = new HashMap<>();
    private Long trainerId = 0L;


    @Override
    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            trainerId++;
            trainer.setId(trainerId);
        }
        trainerMap.put(trainer.getId(), trainer);
        return trainer;
    }

    @Override
    public Trainer findById(Long id) {
        return trainerMap.get(id);
    }

    @Override
    public Trainer findByUserName(String userName) {
        for (Trainer trainer : trainerMap.values()) {
            if (trainer.getUser().getUserName().equals(userName)) {
                return trainer;
            }
        }
        return null;
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }

    @Override
    public Trainer update(Trainer trainer) {
        if (trainerMap.containsKey(trainer.getId())) {
            trainerMap.put(trainer.getId(), trainer);
            return trainer;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        trainerMap.remove(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return trainerMap.values().stream().anyMatch(trainer -> trainer.getUser().getUserName().equals(username));
    }

}
