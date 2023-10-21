package com.web.travel.service;

import com.web.travel.dto.response.RateResDTO;
import com.web.travel.mapper.response.RateResMapper;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RateService {
    @Autowired
    TourRepository tourRepository;
    public List<RateResDTO> getRatesByTour(long id){
        Tour foundTour = tourRepository.findById(id).orElse(null);
        if (foundTour != null) {
            List<Rate> rates = foundTour.getRates().stream().toList();
            return rates.stream().map(rate -> {
                RateResMapper mapper = new RateResMapper();
                return (RateResDTO) mapper.mapToDTO(rate);
            }).toList();
        }
        return new ArrayList<>();
    }
}
