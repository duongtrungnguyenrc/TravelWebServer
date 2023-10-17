package com.web.travel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String img;
    @OneToOne()
    @JoinColumn(name = "paragraphId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Paragraph paragraph;
}
