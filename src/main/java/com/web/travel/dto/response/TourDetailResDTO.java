package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.Hotel;
import com.web.travel.model.enumeration.ETourType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailResDTO {
    private Long id;
    private String name;
    private double adultPrice;
    private double childPrice;
    private String vehicle;
    private ETourType tourType;
    private String depart;
    private String destination;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    private int maxPeople;
    private int currentPeople;
    private String img;
    private TourBlogResDTO blog;
    private List<HotelResDTO> hotels;
}
