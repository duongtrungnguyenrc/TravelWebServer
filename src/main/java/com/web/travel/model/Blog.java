package com.web.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "blog")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String backgroundImg;
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Paragraph> paragraphs;
    public Blog(String backgroundImg){
        this.backgroundImg = backgroundImg;
    }
}
