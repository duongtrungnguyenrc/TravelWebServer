package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.hotel.HotelAddingDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.request.HotelAddingReqMapper;
import com.web.travel.model.Hotel;
import com.web.travel.repository.HotelRepository;
import com.web.travel.service.interfaces.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class HotelService {
    @Autowired
    private HotelRepository repository;
    @Autowired
    private FileUploadService fileUploadService;
    public Hotel getHotelById(long id){
        return repository.findById(id).orElse(null);
    }
    public Page<Hotel> getAllHotel(int page, int limit){
        return repository.findAll(PageRequest.of(page, limit));
    }

    public ResDTO getAllHotelRes(int page, int limit){
        Map<String, Object> response = new HashMap<>();
        Page<Hotel> result = getAllHotel(page, limit);
        response.put("pages", result.getTotalPages());
        response.put("hotels", result.getContent());

        return new ResDTO(
                200,
                true,
                "Lấy khách sạn thành công",
                response
        );
    }

    public ResDTO addHotel(
            MultipartFile image,
            HotelAddingDTO hotelAddingDTO
    ){
        String fileName = null;
        if(image != null){
            try {
                fileName = fileUploadService.uploadFile(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Mapper hotelMapper = new HotelAddingReqMapper();
        Hotel hotel = (Hotel) hotelMapper.mapToObject(hotelAddingDTO);

        hotel.setIllustration(fileName);

        return new ResDTO(
                200,
                true,
                "Thêm mới khách sạn thành công!",
                repository.save(hotel)
        );
    }
}