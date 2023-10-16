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
    private String vehicle;
    private ETourType type;
    private String depart;
    private String location;
    private List<TourDateResDTO> tourDate;
    private int maxPeople;
    private int currentPeople;
    private String img;
    private TourBlogResDTO overview;
    private List<HotelResDTO> hotels;
}
