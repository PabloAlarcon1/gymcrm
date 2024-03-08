package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUserNameIn(List<String> userNames);
    User findByUserName(String userName);

}
