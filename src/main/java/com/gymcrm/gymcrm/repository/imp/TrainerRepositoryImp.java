package com.gymcrm.gymcrm.repository.imp;

import com.gymcrm.gymcrm.model.Trainer;
import com.gymcrm.gymcrm.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Repository
@Transactional
public abstract class TrainerRepositoryImp implements TrainerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, Trainer> trainerDatabase;

    @Override
    public void create(Trainer trainer) {

        entityManager.persist(trainer);

    }

    @Override
    public Optional<Trainer> get(Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> cr = cb.createQuery(Trainer.class);
        Root<Trainer> root = cr.from(Trainer.class);
        cr.select(root).where(cb.equal(root.get("userId"), String.valueOf(id)));
        Query query = entityManager.createQuery(cr);
        List<Trainer> results = query.getResultList();
        return results.isEmpty()? Optional.empty() : Optional.ofNullable(results.get(0));

    }

    @Override
    public List<Trainer> getByCriteria(Map<String, String> criterias) {
        //in cirtierias we can have
        //Map<String, String> criteria = new HashMap<>();
        //criteria.put("trainingType","yoga");
        //criteria.put("usedId","11");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> cr = cb.createQuery(Trainer.class);
        Root<Trainer> root = cr.from(Trainer.class);
        cr.select(root);
        Predicate[] predicates = new Predicate[criterias.size()];
        cr.select(root).where(predicates);
        int index = 0;
        for(Map.Entry<String,String> crit :  criterias.entrySet()){
            predicates[index] = cb.equal(root.get(crit.getKey()), crit.getValue());
            index++;
        }
        cr.where(predicates);
        Query query = entityManager.createQuery(cr);
        List<Trainer> results = query.getResultList();
        return results;
    }

    @Override
    public void update(Trainer trainer) {
        //trainerDatabase.put(trainer.getId(), trainer);
    }

    @Override
    public void delete(Integer id) {
        trainerDatabase.remove(id);
    }
}
