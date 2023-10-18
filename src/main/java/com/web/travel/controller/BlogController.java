package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.BlogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit){
        page = page != null ? page : 1;
        limit = limit != null ? limit : 10;

        long recordNumber = blogService.getDesBlogCount();
        int pageNumber = Math.round((float) recordNumber/limit);

        return ResponseEntity.ok(
                new ResDTO(HttpServletResponse.SC_OK,
                        true,
                        "Blog fetched successfully",
                        blogService.getAllDestinationBlog(
                                page - 1,
                                limit,
                                pageNumber
                        )
                )
        );
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatest(){
        return ResponseEntity.ok(
                new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Blog fetched succcessfully",
                        blogService.getTopLatestPosts(4)
                )
        );
    }

    @GetMapping("/authors")
    public ResponseEntity<?> getAuthorsDesc(){
        return ResponseEntity.ok(
            new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Authors fetched succcessfully",
                    blogService.getListAuthorDesc()
            )
        );
    }
}
