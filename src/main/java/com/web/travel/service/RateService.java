package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.RateReqDTO;
import com.web.travel.dto.response.RateResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.request.RateReqMapper;
import com.web.travel.mapper.response.RateResMapper;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.repository.RateRepository;
import com.web.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RateService {
    @Autowired
    TourRepository tourRepository;
    @Autowired
    RateRepository rateRepository;
    @Autowired
    RateReqMapper reqMapper;
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

    public ResDTO addRate(RateReqDTO ratingDTO){
        Rate rate = (Rate) reqMapper.mapToObject(ratingDTO);
        Map<String, RateResDTO> response = new HashMap<>();
        RateResMapper mapper = new RateResMapper();
        response.put(
                "rateAdded",
                (RateResDTO) mapper.mapToDTO(rateRepository.save(rate))
            );

        return new ResDTO(
                200,
                true,
                "Đã thêm bình luận",
                response
        );
    }
}
