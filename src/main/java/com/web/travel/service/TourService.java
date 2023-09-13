package com.web.travel.service;

import com.web.travel.dto.tour.TourDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TourService {
    public List<TourDTO> getTourDTOList(){
        List<TourDTO> list = new ArrayList<TourDTO>();

        list.add(new TourDTO());
        list.add(new TourDTO());
        list.add(new TourDTO());
        list.add(new TourDTO());
        list.add(new TourDTO());
        list.add(new TourDTO());
        list.add(new TourDTO());
        list.add(new TourDTO());

        return list;
    }
}
