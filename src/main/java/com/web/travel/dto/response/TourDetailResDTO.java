package com.web.travel.dto.response;

import com.web.travel.model.enumeration.ETourType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailResDTO {
    private Long id;
    private String name;
    private double startFrom;
    private String vehicle;
    private double ratedStar;
    private ETourType type;
    private String depart;
    private String location;
    private List<TourDateResDTO> tourDate;
    private int maxPeople;
    private int currentPeople;
    private String img;
    private TourBlogResDTO overview;
    private List<RateResDTO> rates;
    private List<HotelResDTO> hotels;
    private List<ScheduleResDTO> schedules;
    private List<TourResDTO> relevantTours;
}
