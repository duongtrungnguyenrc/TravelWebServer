package com.web.travel.mapper.response;

import com.web.travel.core.DateHandler;
import com.web.travel.dto.response.TourResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;

@Component
public class TourResMapper implements Mapper {
    TourResDTO tourResDTO = new TourResDTO();
    DateHandler dateHandler = new DateHandler();
    @Override
    public Object mapToDTO(Object obj) {
        tourResDTO.setId(((Tour) obj).getId());
        tourResDTO.setImg(((Tour) obj).getImg());
        tourResDTO.setName(((Tour) obj).getName());
        tourResDTO.setLocation(((Tour) obj).getDestination());
        tourResDTO.setPrice(((Tour) obj).getAdultPrice());
        Date depart = ((Tour) obj).getDepartDate();
        Date end = ((Tour) obj).getEndDate();
        tourResDTO.setDuration(dateHandler.getDiffDay(end, depart));
        tourResDTO.setMaxPeople(((Tour) obj).getMaxPeople());
        String typeDto = "";
        String typeTitle = "";
        switch (((Tour) obj).getTourType().toString()) {
            case "TYPE_SAVING" -> {
                typeDto = "saving";
                typeTitle = "Tour tiết kiệm";
            }
            case "TYPE_SPECIAL" -> {
                typeDto = "special";
                typeTitle = "Tour đặc biệt";
            }
            case "TYPE_NORMAL" -> {
                typeTitle = "Tour thông thường";
                typeDto = "normal";
            }
            case "TYPE_POPULAR" -> {
                typeTitle = "Tour phổ biến";
                typeDto = "popular";
            }
            default -> {
            }
        }
        tourResDTO.setType(typeDto);
        tourResDTO.setTypeTitle(typeTitle);

        Collection<Rate> rates = ((Tour) obj).getRates();

        List<Integer> points = rates.stream().map(
                Rate::getPoint
        ).toList();

        IntSummaryStatistics intSummaryStatistics = points
                .stream()
                .mapToInt(point -> point)
                .summaryStatistics();

        tourResDTO.setRatedStar(intSummaryStatistics.getAverage());
        return tourResDTO;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
