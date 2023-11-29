package com.web.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TourGeneralResDTO {
    private Long id;
    private String name;
    private double price;
    private double ratedStar;
    private String depart;
    private String location;
    private int maxPeople;
    private int duration;
    private String img;
    private String type;
    private String typeTitle;

    public TourGeneralResDTO(){
        id = 1L;
        name = "Deluxe Double with New York City View";
        price = 59;
        depart = "Ninh Thuan";
        img = "/images/home-slider-3.jpg";
        ratedStar = 4.9;
        location = "Phu Quy";
        maxPeople = 3;
        duration = 3;
    }
}