package com.web.travel.service;

import com.web.travel.dto.tour.ListTourDTO;
import com.web.travel.dto.tour.TourDTO;
import com.web.travel.mapper.TourMapper;
import com.web.travel.model.Tour;
import com.web.travel.model.enumeration.ETourType;
import com.web.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourService {
    @Autowired
    TourRepository tourRepository;
    public List<ListTourDTO> getTourDTOListGroupByType(){
        List<ListTourDTO> listTourDTOS = new ArrayList<>();
        List<TourDTO> list = new ArrayList<>();

        ListTourDTO dto1 = new ListTourDTO();
        dto1.setType("Popular");
        dto1.setTours(tourRepository.findByTourType(ETourType.TYPE_POPULAR)
                .stream().map(
                        tour -> {
                            TourMapper mapper = new TourMapper();
                            return (TourDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourDTO dto2 = new ListTourDTO();
        dto2.setType("Normal");
        dto2.setTours(tourRepository.findByTourType(ETourType.TYPE_NORMAL)
                .stream().map(
                        tour -> {
                            TourMapper mapper = new TourMapper();
                            return (TourDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourDTO dto3 = new ListTourDTO();
        dto3.setType("Special");
        dto3.setTours(tourRepository.findByTourType(ETourType.TYPE_SPECIAL)
                .stream().map(
                        tour -> {
                            TourMapper mapper = new TourMapper();
                            return (TourDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourDTO dto4 = new ListTourDTO();
        dto4.setType("Saving");
        dto4.setTours(tourRepository.findByTourType(ETourType.TYPE_SAVING)
                .stream().map(
                        tour -> {
                            TourMapper mapper = new TourMapper();
                            return (TourDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        listTourDTOS.add(dto1);
        listTourDTOS.add(dto2);
        listTourDTOS.add(dto3);
        listTourDTOS.add(dto4);

        return listTourDTOS;
    }

    public List<TourDTO> getAllTour(){
        return tourRepository.findAll().stream().map(tour -> {
            TourMapper tourMapper = new TourMapper();
            return (TourDTO) tourMapper.mapToDTO(tour);
        }).toList();
    }
}
