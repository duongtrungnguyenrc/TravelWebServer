package com.web.travel.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Data
public class TourDTO {
    private String name;
    private double price;
    private double ratedStar;
    private String location;
    private int maxPeople;
    private int time;
    private String img;

    public TourDTO(){
        name = "Deluxe Double with New York City View";
        price = 59;
        img = "/images/home-slider-3.jpg";
        ratedStar = 4.9;
        location = "Phu Quy";
        maxPeople = 3;
        time = 3;
    }
}
