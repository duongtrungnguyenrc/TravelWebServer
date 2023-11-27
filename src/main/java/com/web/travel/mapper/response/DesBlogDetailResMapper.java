package com.web.travel.mapper.response;

import com.web.travel.dto.response.DesBlogDetailResDTO;
import com.web.travel.dto.response.ParagraphImageResDTO;
import com.web.travel.dto.response.ParagraphResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Paragraph;
import com.web.travel.repository.ParagraphImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class DesBlogDetailResMapper implements Mapper {
    @Autowired
    ParagraphImgRepository paragraphImgRepository;
    @Override
    public Object mapToDTO(Object obj) {
        DestinationBlog destinationBlog = (DestinationBlog) obj;
        DesBlogDetailResDTO dto = new DesBlogDetailResDTO();
        dto.setId(destinationBlog.getId());
        dto.setType(destinationBlog.getType());
        dto.setImg(destinationBlog.getBlog().getBackgroundImg());
        dto.setTitle(destinationBlog.getTitle());
        dto.setAuthor(destinationBlog.getUser().getFullName());
        dto.setTime(destinationBlog.getPostDate());
        
        List<ParagraphResDTO> paragraphs = destinationBlog
                .getBlog()
                .getParagraphs()
                .stream().sorted(
                    new Comparator<Paragraph>() {
                        @Override
                        public int compare(Paragraph o1, Paragraph o2) {
                            return o1.getOrder() - o2.getOrder();
                        }
                    }
                )
                .map(paragraph -> {
                    ParagraphResDTO paragraphResDTO = new ParagraphResDTO();
                    paragraphImgRepository.findByParagraph(paragraph)
                        .ifPresent(item -> {
                            ParagraphImageResDTO img = new ParagraphImageResDTO();
                            img.setSrc(item.getImg());
                            img.setName(item.getName());

                            paragraphResDTO.setImage(img);
                        });
                    paragraphResDTO.setContent(paragraph.getContent());
                    paragraphResDTO.setId(paragraph.getId());
                    
                    return paragraphResDTO;
                }).toList();
        dto.setParagraphs(paragraphs);

        return dto;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
