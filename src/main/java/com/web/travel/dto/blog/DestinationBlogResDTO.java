package com.web.travel.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationBlogResDTO {
    private Long id;
    private String title;
    private String type;
    private Date time;
    private String author;
    private String img;
}
