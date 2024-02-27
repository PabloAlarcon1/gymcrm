package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Integer>, JpaSpecificationExecutor<Trainer> {

    @Query("SELECT t FROM Trainer t WHERE t.user.userName = :userName")
    Optional<Trainer> findByUserUserName(@Param("userName") String userName);
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Trainer t WHERE t.user.userName = :userName")
    boolean existsByUserUserName(@Param("userName") String userName);

    @Query("SELECT t FROM Trainer t WHERE t.specialization.name = :specializationName")
    List<Trainer> getByCriteria(@Param("specializationName") String specializationName);


}
