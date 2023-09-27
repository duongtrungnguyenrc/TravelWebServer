package com.web.travel.service;

import com.web.travel.dto.response.DestinationBlogResDTO;
import com.web.travel.mapper.response.DestinationBlogResMapper;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.repository.DestinationBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    @Autowired
    DestinationBlogRepository desRepository;
    public Map<String, Object> getAllDestinationBlog(int page, int limit, int pages){
        Page<DestinationBlog> list = desRepository.findAll(PageRequest.of(page, limit));
        Map<String, Object> result = new HashMap<>();

        List<DestinationBlogResDTO> listDTO = list.stream().map(blog -> {
            Mapper mapper = new DestinationBlogResMapper();
            return (DestinationBlogResDTO) mapper.mapToDTO(blog);
        }).toList();

        result.put("pages", pages == 0 ? 1 : pages);
        result.put("blogs", listDTO);
        return result;
    }

    public long getDesBlogCount(){
        return desRepository.count();
    }
}
