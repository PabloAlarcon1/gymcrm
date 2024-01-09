package com.gymcrm.gymcrm.service;

import com.gymcrm.gymcrm.dao.UserDao;
import com.gymcrm.gymcrm.gymcrm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService {

    private final UserDao userDao;


    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User update(User user) {
        return userDao.update(user);
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }
}
