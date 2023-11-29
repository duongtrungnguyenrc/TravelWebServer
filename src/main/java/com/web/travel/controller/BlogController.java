package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.BlogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/blog")
@CrossOrigin(origins = "*")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ){
        return ResponseEntity.ok(
                new ResDTO(HttpServletResponse.SC_OK,
                        true,
                        "Blog fetched successfully",
                        blogService.getAllDestinationBlog(
                                page - 1,
                                limit
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
                        "Blog fetched successfully",
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
                "Authors fetched successfully",
                blogService.getListAuthorDesc()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable long id){
        ResDTO response = blogService.getById(id);
        if(response.isStatus()){
            return ResponseEntity.ok(
                    response
            );
        }
        return ResponseEntity.badRequest().body(
                response
        );
    }
}
