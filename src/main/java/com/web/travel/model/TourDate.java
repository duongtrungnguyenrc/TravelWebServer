package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.enumeration.ETourDateType;
import com.web.travel.model.enumeration.ETourType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tour_date")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private ETourDateType dateType;
    private double adultPrice;
    private double childPrice;
    @ManyToOne
    @JoinColumn(name = "tourId")
    private Tour tour;
}
