package com.gymcrm.gymcrm.dao;

import com.gymcrm.gymcrm.gymcrm.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    User findById(Long id);
    List<User> findAll();
    User update(User user);
    User findByUserName(String userName);
    void delete(Long id);
}
