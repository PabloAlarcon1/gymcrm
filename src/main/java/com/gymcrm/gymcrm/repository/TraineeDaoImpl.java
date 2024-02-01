package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TraineeDaoImpl implements TraineeDao {

    private final Map<Long, Trainee> traineeMap = new HashMap<>();
    private Long traineeId = 0L;

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            traineeId++;
            trainee.setId(traineeId);
        }
        traineeMap.put(trainee.getId(), trainee);
        return trainee;
    }

    @Override
    public Trainee findById(Long id) {
        return traineeMap.get(id);
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }

    @Override
    public Trainee findByUserName(String userName) {
        for (Trainee trainee : traineeMap.values()) {
            if (trainee.getUser().getUserName().equals(userName)) {
                return trainee;
            }
        }
        return null; // Retornar null si no se encuentra ningún Trainee con el nombre de usuario dado
    }

    @Override
    public Trainee update(Trainee trainee) {
        if (traineeMap.containsKey(trainee.getId())) {
            traineeMap.put(trainee.getId(), trainee);
            return trainee;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        traineeMap.remove(id);

    }

    @Override
    public boolean existsByUsername(String userName) {
        return traineeMap.values().stream().anyMatch(trainee -> trainee.getUser().getUserName().equals(userName));
    }
}
