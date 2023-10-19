package com.web.travel.repository.custom;

import com.web.travel.model.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomTourRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<Tour> getRelevantTourByDestination(String depart, String destination, int numberOfTour){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tour> criteriaQuery = builder.createQuery(Tour.class);
        Root<Tour> root = criteriaQuery.from(Tour.class);

        String normalDestinationStr = StringUtils.stripAccents(destination),
            normalDepartStr = StringUtils.stripAccents(depart);
        normalDestinationStr = normalDestinationStr.toLowerCase();
        normalDepartStr = normalDepartStr.toLowerCase();
        Predicate destinationLikePredicate = builder.like(root.get("destination"), '%' + normalDestinationStr + '%');
        Predicate departLikePredicate = builder.like(root.get("depart"), '%' + normalDepartStr + '%');
        Predicate desLikeOrDepartLike = builder.or(destinationLikePredicate, departLikePredicate);

        criteriaQuery.select(root).where(desLikeOrDepartLike);

        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(numberOfTour)
                .getResultList();
    }
}
