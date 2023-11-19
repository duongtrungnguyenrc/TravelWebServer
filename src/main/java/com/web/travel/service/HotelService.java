package com.web.travel.service;

import com.web.travel.model.Hotel;
import com.web.travel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {
    @Autowired
    private HotelRepository repository;
    public Hotel getHotelById(long id){
        return repository.findById(id).orElse(null);
    }
}
