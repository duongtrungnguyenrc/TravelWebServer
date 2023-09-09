package com.web.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "paragraph")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name = "blogId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Blog blog;

    @OneToMany(mappedBy = "paragraph", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<ParagraphImg> paragraphImgs;
}
