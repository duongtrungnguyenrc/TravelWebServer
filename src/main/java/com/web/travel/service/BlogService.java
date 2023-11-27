package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.blog.BlogAddingReqDTO;
import com.web.travel.dto.response.DesBlogDetailResDTO;
import com.web.travel.dto.response.DestinationBlogResDTO;
import com.web.travel.mapper.request.BlogAddReqMapper;
import com.web.travel.mapper.response.DesBlogDetailResMapper;
import com.web.travel.mapper.response.DestinationBlogResMapper;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Paragraph;
import com.web.travel.model.ParagraphImg;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EStatus;
import com.web.travel.repository.DestinationBlogRepository;
import com.web.travel.repository.ParagraphImgRepository;
import com.web.travel.repository.custom.CustomDestinationBlogRepository;
import com.web.travel.repository.custom.enumeration.ESortType;
import com.web.travel.service.cloudinary.FilesValidation;
import com.web.travel.service.interfaces.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class BlogService {
    @Autowired
    DestinationBlogRepository desRepository;
    @Autowired
    CustomDestinationBlogRepository customDesBlogRepository;
    @Autowired
    BlogAddReqMapper mapper;
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    ParagraphImgRepository paragraphImgRepository;
    @Autowired
    DesBlogDetailResMapper desBlogDetailResMapper;
    @Autowired
    FilesValidation fileValidator;
    public Map<String, Object> getAllDestinationBlog(int page, int limit){
        Page<DestinationBlog> list = desRepository.findAllByOrderByPostDateDesc(PageRequest.of(page, limit));
        Map<String, Object> result = new HashMap<>();

        int pages = list.getTotalPages();

        List<DestinationBlogResDTO> listDTO = list.stream().map(blog -> {
            Mapper mapper = new DestinationBlogResMapper();
            DestinationBlogResDTO dto = (DestinationBlogResDTO) mapper.mapToDTO(blog);
            Paragraph paragraph = blog.getBlog().getParagraphs().stream().toList().get(0);
            dto.setDescription(paragraph.getContent());
            return dto;
        }).toList();

        result.put("pages", pages);
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

    public ResDTO getById(long id){
        DesBlogDetailResDTO dto;
        Optional<DestinationBlog> foundBlog = desRepository.findById(id);

        if(foundBlog.isPresent()){
            dto = (DesBlogDetailResDTO) desBlogDetailResMapper.mapToDTO(foundBlog.get());
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Blog fetched successfully!",
                    dto
            );
        }else{
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Could not found blog with id: " + id,
                    null
            );
        }
    }

    public ResDTO addBlog(Principal principal, BlogAddingReqDTO blogDto, MultipartFile thumbnail, MultipartFile[] images){
        blogDto.setUserEmail(principal.getName());
        DestinationBlog blog = (DestinationBlog) mapper.mapToObject(blogDto);

        String backgroundImg = "";
        List<ParagraphImg> paragraphImgs = new ArrayList<>();
        try{
            backgroundImg = fileUploadService.uploadFile(thumbnail);
            List<String> fileNames = null;
            if(fileValidator.validate(images) != EStatus.STATUS_EMPTY_FILE){
                fileNames = fileUploadService.uploadMultiFile(images);
            }

            List<Paragraph> addedParagraph = blog
                    .getBlog()
                    .getParagraphs()
                    .stream().toList();

            if(fileNames != null && fileNames.size() <= addedParagraph.size()) {
                for (int i = 0; i < fileNames.size(); i++) {
                    ParagraphImg paragraphImg = new ParagraphImg();

                    paragraphImg.setImg(fileNames.get(i));
                    paragraphImg.setName(blogDto.getParagraphs().get(i).getImageName());
                    paragraphImg.setParagraph(addedParagraph.get(i));
                    paragraphImgRepository.save(paragraphImg);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        blog.getBlog().setBackgroundImg(backgroundImg);

        Long id = desRepository.save(blog).getId();

        Map<String, Long> response = new HashMap<>();
        response.put("id", id);

        return new ResDTO(
            HttpServletResponse.SC_OK,
            true,
            "Added blog successfully",
            response
        );
    }
}
