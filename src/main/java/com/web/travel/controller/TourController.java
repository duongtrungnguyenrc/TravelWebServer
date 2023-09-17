package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.tour.ListTourDTO;
import com.web.travel.dto.tour.TourDTO;
import com.web.travel.model.Tour;
import com.web.travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourController {
    @Autowired
    TourService tourService;

    @GetMapping("/all")
    public Object getAll(){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.getAllTour())
        );
    }
    @GetMapping("/all/type")
    public Object getListTourByType(){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.getTourDTOListGroupByType())
        );
    }
}
