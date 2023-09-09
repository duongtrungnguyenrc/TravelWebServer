package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String tittle;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotBlank
    private Date postDate;
    @OneToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    public DestinationBlog(String tittle, Date postDate, Blog blog) {
        this.tittle = tittle;
        this.postDate = postDate;
        this.blog = blog;
    }
}
