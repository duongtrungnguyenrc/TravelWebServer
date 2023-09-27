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
        tourResDTO.setTime(dateHandler.getDiffDay(end, depart));
        tourResDTO.setMaxPeople(((Tour) obj).getMaxPeople());
        String typeDto = "";
        switch (((Tour) obj).getTourType().toString()) {
            case "TYPE_SAVING" -> typeDto = "saving";
            case "TYPE_SPECIAL" -> typeDto = "special";
            case "TYPE_NORMAL" -> typeDto = "normal";
            case "TYPE_POPULAR" -> typeDto = "popular";
            default -> {
            }
        }
        tourResDTO.setType(typeDto);

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
