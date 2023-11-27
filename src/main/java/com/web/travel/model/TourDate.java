package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.enumeration.ETourDateType;
import com.web.travel.model.enumeration.ETourType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tour_date")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date departDate;
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private ETourDateType dateType;
    private double adultPrice;
    private double childPrice;
    private int currentPeople;
    private int maxPeople;
    @ManyToOne
    @JoinColumn(name = "tourId")
    private Tour tour;
    @OneToMany(mappedBy = "tourDate")
    private List<Order> orders;

    public boolean isFull(){
        return currentPeople >= maxPeople;
    }
    public boolean canBook(int adults){
        return adults <= (maxPeople - currentPeople);
    }
}
