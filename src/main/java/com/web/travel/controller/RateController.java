package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.response.RateResDTO;
import com.web.travel.service.RateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rate")
public class RateController {
    @Autowired
    RateService rateService;
    @GetMapping("/tour/{tourId}")
    public ResponseEntity<?> getRateByTour(@PathVariable("tourId") long tourId){
        List<RateResDTO> dtos = rateService.getRatesByTour(tourId);
        return ResponseEntity.ok(
            new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Rates fetched successfully",
                    dtos
            )
        );
    }
}
