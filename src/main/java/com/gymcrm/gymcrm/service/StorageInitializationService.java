package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.dao.TrainingDao;
import com.gymcrm.gymcrm.gymcrm.model.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;

@Service
@Slf4j
@Getter
@Setter
public class StorageInitializationService {

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingDao trainingDao;

    @PostConstruct
    public void initializeStorage() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/datos.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String currentEntity = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Trainee:")) {
                    currentEntity = "Trainee";
                } else if (line.startsWith("Trainer:")) {
                    currentEntity = "Trainer";
                } else if (line.startsWith("Training:")) {
                    currentEntity = "Training";
                } else if (currentEntity != null) {
                    switch (currentEntity) {
                        case "Trainee":
                            processTrainee(line);
                            break;
                        case "Trainer":
                            processTrainer(line);
                            break;
                        case "Training":
                            processTraining(line);
                            break;
                    }

                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTrainee(String line) {
        String[] parts = line.split(",");
        Long id = Long.parseLong(parts[0].split("=")[1]);
        LocalDate dateOfBirth = LocalDate.parse(parts[1].split("=")[1]);
        String address = parts[2].split("=")[1];
        Long userId = Long.parseLong(parts[3].split("=")[1]);
        User user = new User();
        user.setId(userId);

        Trainee trainee = new Trainee(id, dateOfBirth, address, user);
        traineeDao.save(trainee);
    }

    private void processTrainer(String line) {
        String[] parts = line.split(",");
        Long id = Long.parseLong(parts[0].split("=")[1]);
        String specialization = parts[1].split("=")[1];
        Specialization specialization1 = new Specialization();
        specialization1.setName(specialization);
        Long userId = Long.parseLong(parts[2].split("=")[1]);
        User user = new User();
        user.setId(userId);

        Trainer trainer = new Trainer(id, specialization1, user);
        trainerDao.save(trainer);
    }

    private void processTraining(String line) {
        String[] parts = line.split(",");
        Long id = Long.parseLong(parts[0].split("=")[1]);
        Long traineeId = Long.parseLong(parts[1].split("=")[1]);
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        Long trainerId = Long.parseLong(parts[2].split("=")[1]);
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        String trainingName = parts[3].split("=")[1];
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingName);
        // tengo dudas en qué pongo en training en vez de trainingTypeId
        Long trainingTypeId = Long.parseLong(parts[4].split("=")[1]);
        trainingType.setId(trainingTypeId);
        LocalDate trainingDate = LocalDate.parse(parts[5].split("=")[1]);
        Integer trainingDuration = Integer.parseInt(parts[6].split("=")[1]);

        Training training = new Training(id, trainee, trainer, trainingType, trainingTypeId, trainingDate, trainingDuration);
        trainingDao.save(training);

    }




}
