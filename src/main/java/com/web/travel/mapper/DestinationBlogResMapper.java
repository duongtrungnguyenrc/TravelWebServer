package com.web.travel.mapper;

import com.web.travel.dto.blog.DestinationBlogResDTO;
import com.web.travel.model.DestinationBlog;

public class DestinationBlogResMapper implements Mapper{
    @Override
    public Object mapToDTO(Object obj) {
        DestinationBlogResDTO dto = new DestinationBlogResDTO();
        if(obj instanceof DestinationBlog){
            obj = (DestinationBlog) obj;
            dto.setId(((DestinationBlog) obj).getId());
            dto.setTime(((DestinationBlog) obj).getPostDate());
            dto.setAuthor(((DestinationBlog) obj).getUser().getFullName());
            dto.setType(((DestinationBlog) obj).getType());
            dto.setTitle(((DestinationBlog) obj).getTitle());
            dto.setImg(((DestinationBlog) obj).getBlog().getBackgroundImg());
        }
        return dto;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
