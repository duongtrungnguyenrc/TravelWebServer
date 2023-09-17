package com.web.travel.mapper;

import com.web.travel.core.DateHandler;
import com.web.travel.dto.tour.TourDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;

@Component
public class TourMapper implements Mapper {
    TourDTO tourDTO = new TourDTO();
    DateHandler dateHandler = new DateHandler();
    @Override
    public Object mapToDTO(Object obj) {
        tourDTO.setId(((Tour) obj).getId());
        tourDTO.setImg(((Tour) obj).getImg());
        tourDTO.setName(((Tour) obj).getName());
        tourDTO.setLocation(((Tour) obj).getDestination());
        tourDTO.setPrice(((Tour) obj).getAdultPrice());
        Date depart = ((Tour) obj).getDepartDate();
        Date end = ((Tour) obj).getEndDate();
        tourDTO.setTime(dateHandler.getDiffDay(end, depart));
        tourDTO.setMaxPeople(((Tour) obj).getMaxPeople());
        tourDTO.setType(((Tour) obj).getTourType().toString());

        Collection<Rate> rates = ((Tour) obj).getRates();

        List<Integer> points = rates.stream().map(
                Rate::getPoint
        ).toList();

        IntSummaryStatistics intSummaryStatistics = points
                .stream()
                .mapToInt(point -> point)
                .summaryStatistics();

        tourDTO.setRatedStar(intSummaryStatistics.getAverage());
        return tourDTO;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
