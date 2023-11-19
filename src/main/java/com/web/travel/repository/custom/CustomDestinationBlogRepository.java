package com.web.travel.repository.custom;

import com.web.travel.model.DestinationBlog;
import com.web.travel.model.User;
import com.web.travel.repository.custom.enumeration.ESortType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomDestinationBlogRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<DestinationBlog> findTopLatestPosts(int top){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DestinationBlog> criteriaQuery = builder.createQuery(DestinationBlog.class);
        Root<DestinationBlog> root = criteriaQuery.from(DestinationBlog.class);

        criteriaQuery.select(root).orderBy(builder.desc(root.get("postDate")));
        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(top)
                .getResultList();
    }

    public List<User> findTopAuthor(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        root.join("destinationBlogs", JoinType.LEFT);
        criteriaQuery.select(root)
                .groupBy(root.get("id"))
                .orderBy(builder.desc(builder.count(root)));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
