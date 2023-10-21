package com.web.travel.service;

import com.web.travel.dto.response.DestinationBlogResDTO;
import com.web.travel.mapper.response.DestinationBlogResMapper;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Paragraph;
import com.web.travel.model.User;
import com.web.travel.repository.DestinationBlogRepository;
import com.web.travel.repository.custom.CustomDestinationBlogRepository;
import com.web.travel.repository.custom.enumeration.ESortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    @Autowired
    DestinationBlogRepository desRepository;
    @Autowired
    CustomDestinationBlogRepository customDesBlogRepository;
    public Map<String, Object> getAllDestinationBlog(int page, int limit, int pages){
        Page<DestinationBlog> list = customDesBlogRepository.findAllOrderByDate(ESortType.TYPE_DESC, PageRequest.of(page, limit));
        Map<String, Object> result = new HashMap<>();

        List<DestinationBlogResDTO> listDTO = list.stream().map(blog -> {
            Mapper mapper = new DestinationBlogResMapper();
            DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
            Paragraph paragraph = blog.getBlog().getParagraphs().stream().toList().get(0);
            dto.setDescription(paragraph.getContent());
            return dto;
        }).toList();


        result.put("pages", pages == 0 ? 1 : pages);
        result.put("posts", listDTO);
        return result;
    }

    public long getDesBlogCount(){
        return desRepository.count();
    }

    public List<DestinationBlogResDTO> getTopLatestPosts(int top){
        List<DestinationBlog> top4LatestBlogs = customDesBlogRepository.findTopLatestPosts(4);
        return top4LatestBlogs.stream().map(blog -> {
            Mapper mapper = new DestinationBlogResMapper();
            DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
            Paragraph paragraph = blog.getBlog().getParagraphs().stream().toList().get(0);
            dto.setDescription(paragraph.getContent());
            return dto;
        }).toList();
    }

    public List<Map<String, Object>> getListAuthorDesc(){
        List<User> authors = customDesBlogRepository.findTopAuthor();
        List<Map<String, Object>> result = new ArrayList<>();
        authors.forEach(author -> {
            Map<String, Object> record = new HashMap<>();
            record.put("id", author.getId());
            record.put("name", author.getFullName());
            result.add(record);
        });
        return result;
    }
}
