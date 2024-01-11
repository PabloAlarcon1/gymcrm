package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.TraineeDao;
import com.gymcrm.gymcrm.dao.TrainerDao;
import com.gymcrm.gymcrm.dao.UserDao;
import com.gymcrm.gymcrm.gymcrm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProfileService {

    private static final int PASSWORD_LENGTH = 10;

    @Autowired
    private UserDao userDao;

    public boolean userNameExists(String userName) {
        User user = userDao.findByUserName(userName);
        return user != null;
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String userName = baseUsername;
        int counter = 1;

        while (userNameExists(userName)) {
            userName = baseUsername + counter;
            counter++;
        }

        return userName;
    }

    public String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }



}
