package com.web.travel.repository;

import com.web.travel.model.Paragraph;
import com.web.travel.model.ParagraphImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ParagraphImgRepository extends JpaRepository<ParagraphImg, Long> {
    public Optional<ParagraphImg> findByParagraph(Paragraph paragraph);

    @Modifying
    @Query("DELETE FROM ParagraphImg p WHERE p.paragraph.id = ?1")
    void deleteByParagraphId(long id);
}
