package com.web.travel.service;

import com.web.travel.dto.blog.DestinationBlogResDTO;
import com.web.travel.mapper.DestinationBlogResMapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.repository.DestinationBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    DestinationBlogRepository desRepository;
    public List<DestinationBlogResDTO> getAllDestinationBlog(){
        List<DestinationBlog> list = desRepository.findAll();
        return list.stream().map(blog -> {
            DestinationBlogResMapper mapper = new DestinationBlogResMapper();
            return (DestinationBlogResDTO) mapper.mapToDTO(blog);
        }).toList();
    }
}
