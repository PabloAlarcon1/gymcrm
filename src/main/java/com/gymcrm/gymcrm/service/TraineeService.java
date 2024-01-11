package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class TraineeService {

    private final TraineeDao traineeDao;
    private final UserService userService;
    private final ProfileService profileService;




    public Trainee saveTrainee(Trainee trainee) {
        String userName = profileService.generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName());
        String password = profileService.generatePassword();
        User user = trainee.getUser();
        user.setUserName(userName);
        user.setPassword(password);
        userService.save(user);
        return traineeDao.save(trainee);
    }

    public Trainee getTraineeById(Long id) {
        return traineeDao.findById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeDao.findAll();
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeDao.update(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeDao.delete(id);
    }

}
