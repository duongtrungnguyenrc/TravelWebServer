package com.web.travel.repository;

import com.web.travel.model.DestinationBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationBlogRepository extends JpaRepository<DestinationBlog, Long> {
    public Page<DestinationBlog> findAllByOrderByPostDateDesc(Pageable pageable);
    public Page<DestinationBlog> findByTitleContaining(String keyword, Pageable pageable);
}
