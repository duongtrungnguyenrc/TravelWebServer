package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(length = 100000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "blogId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Blog blog;

    @OneToMany(mappedBy = "paragraph", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
//    @JsonIgnore
    private Collection<ParagraphImg> paragraphImgs;
}
