package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin(origins = "*")
public class TourController {
    @Autowired
    TourService tourService;
    @GetMapping("/all")
    public Object getAll(
            @RequestParam(value = "page", required =false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required =false, defaultValue = "10") Integer limit){

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
    public ResponseEntity<?> getListTourByType(@RequestParam(name = "type") String type){
        return ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "Lấy dữ liệu thành công",
                        tourService.findTourByType(type))
        );
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getTourDetail(@RequestParam("id") Long id){
        Object response = tourService.getResponseTourById(id);
        String message = "";

        message = response == null ? "Tour không tồn tại!" : "Lấy tour thành công!";

        return ResponseEntity.ok(
                new ResDTO(
                        HttpStatus.OK.value(),
                        true,
                        message,
                        response
                )
        );
    }
}
