package com.web.travel.controller.admin;

import com.web.travel.dto.request.admin.tour.TourAddingDTO;
import com.web.travel.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/tour")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TourAdminController {
    private final TourService tourService;
    @Operation(summary = "Add new tour")
    @PostMapping("/add")
    public ResponseEntity<?> addTour(
            @RequestPart("tour") TourAddingDTO tour,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("paragraphImages") MultipartFile[] images
    ){
        return ResponseEntity.ok(
                tourService.add(tour, thumbnail, images)
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateTour(
            @PathVariable long id,
            @RequestPart("tour") TourAddingDTO tour,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("paragraphImages") MultipartFile[] images
    ){
        return ResponseEntity.ok(
                tourService.updateTour(id, tour, thumbnail, images)
        );
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteTour(@PathVariable long id){
        return ResponseEntity.ok(
                tourService.deleteTour(id)
        );
    }
}
