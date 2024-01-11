package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.dao.UserDao;
import com.gymcrm.gymcrm.gymcrm.model.Trainee;
import com.gymcrm.gymcrm.gymcrm.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private final Map<Long, User> userMap = new HashMap<>();
    private Long userId = 0L;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            userId++;
            user.setId(userId);
        }
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }
    @Override
    public User update(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public User findByUserName(String userName) {
        return userMap.get(userName);
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }
}
