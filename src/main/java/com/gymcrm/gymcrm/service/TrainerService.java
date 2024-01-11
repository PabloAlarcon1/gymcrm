package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.gymcrm.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrainerService {

    private final TrainerDao trainerDao;
    private final UserService userService;
    private final ProfileService profileService;



    public Trainer saveTrainer(Trainer trainer) {
        String userName = profileService.generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName());
        String userPassword = profileService.generatePassword();
        User user = trainer.getUser();
        user.setUserName(userName);
        user.setPassword(userPassword);
        userService.save(user);
        return trainerDao.save(trainer);
    }

    public Trainer getTrainerById(Long id) {
        return trainerDao.findById(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerDao.findAll();
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerDao.update(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerDao.delete(id);
    }
}
