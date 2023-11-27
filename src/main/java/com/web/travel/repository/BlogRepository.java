package com.web.travel.repository;

import com.web.travel.model.Blog;
import com.web.travel.model.Paragraph;
import com.web.travel.model.ParagraphImg;
import com.web.travel.model.TourBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("DELETE FROM Blog p WHERE p.id = ?1")
    void deleteByBlogId(long id);
}
