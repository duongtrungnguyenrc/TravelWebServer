package com.web.travel.controller;

import com.web.travel.dto.tour.TourDTO;
import com.web.travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourController {
    @Autowired
    TourService tourService;

    @GetMapping("/all")
    public ResponseEntity<List<TourDTO>> getListTour(){
        return ResponseEntity.ok(
                tourService.getTourDTOList()
        );
    }
}
