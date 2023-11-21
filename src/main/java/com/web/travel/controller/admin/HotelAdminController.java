package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.hotel.HotelAddingDTO;
import com.web.travel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/admin/hotel")
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class HotelAdminController {
    private final HotelService hotelService;

    @GetMapping("")
    public ResponseEntity<?> getAllHotel(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ){
       return ResponseEntity.ok(
               hotelService.getAllHotelRes(page - 1, limit)
       );
    }

    @PostMapping()
    public ResponseEntity<?> addHotel(
            @RequestPart("image") MultipartFile image,
            @RequestPart("hotel") HotelAddingDTO hotelAddingDTO
    ){
        return ResponseEntity.ok(
                hotelService.addHotel(image, hotelAddingDTO)
        );
    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateHotel(
            @PathVariable("id") long id,
            @RequestPart("image") MultipartFile image,
            @RequestPart("hotel") HotelAddingDTO hotelAddingDTO
    ){
        return ResponseEntity.ok(
                hotelService.updateHotel(id, image, hotelAddingDTO)
        );
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable long id){
        return ResponseEntity.ok(
                hotelService.deleteHotel(id)
        );
    }
}
