package com.web.travel.repository;

import com.web.travel.model.Paragraph;
import com.web.travel.model.ParagraphImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParagraphImgRepository extends JpaRepository<ParagraphImg, Long> {
    public Optional<ParagraphImg> findByParagraph(Paragraph paragraph);
}
