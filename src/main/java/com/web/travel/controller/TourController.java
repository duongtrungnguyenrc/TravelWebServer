package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tour")
public class TourController {
    @Autowired
    TourService tourService;

    @GetMapping("/all")
    public Object getAll(@RequestParam(value = "page", required =false) Integer page, @RequestParam(value = "limit", required =false) Integer limit){
        page = page != null ? page : 1;
        limit = limit != null ? limit : 10;

        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.getAllTour(page - 1, limit))
        );
    }
    @GetMapping("/all/type")
    public Object getListTourGroupByType(){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.getTourDTOListGroupByType())
        );
    }

    @GetMapping("/type")
    public Object getListTourByType(@RequestParam(name = "type") String type){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.findTourByType(type))
        );
    }
}
