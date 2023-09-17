package com.web.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "schedule")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String time;
    private String content;
    @ManyToOne
    @JoinColumn(name = "tourId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Tour tour;

    public Schedule(String time, String content, Tour tour) {
        this.time = time;
        this.content = content;
        this.tour = tour;
    }
}
