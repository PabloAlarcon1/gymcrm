package com.gymcrm.gymcrm.repository;

import com.gymcrm.gymcrm.model.Trainee;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Integer>, JpaSpecificationExecutor<Trainee> {


    @Query("SELECT t FROM Trainee t WHERE t.user.userName = :userName")
    Optional<Trainee> findByUserUserName(@Param("userName") String userName);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Trainee t WHERE t.user.userName = :userName")
    boolean existsByUserUserName(@Param("userName") String userName);

    default List<Trainee> getByCriteria(Map<String, String> criterias) {
        Specification<Trainee> spec = (root, query, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[criterias.size()];
            int index = 0;
            for (Map.Entry<String, String> entry : criterias.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                Path<String> property = root.get(key);
                predicates[index++] = criteriaBuilder.equal(property, value);
            }
            return criteriaBuilder.and(predicates);
        };
        return findAll(spec);
    }
}
