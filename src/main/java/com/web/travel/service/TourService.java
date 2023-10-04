package com.web.travel.service;

import com.web.travel.dto.response.ListTourResDTO;
import com.web.travel.dto.response.TourDetailResDTO;
import com.web.travel.dto.response.TourResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.response.TourDetailResMapper;
import com.web.travel.mapper.response.TourResMapper;
import com.web.travel.model.Blog;
import com.web.travel.model.Paragraph;
import com.web.travel.model.Tour;
import com.web.travel.model.TourBlog;
import com.web.travel.model.enumeration.ETourType;
import com.web.travel.repository.BlogRepository;
import com.web.travel.repository.TourBlogRepository;
import com.web.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TourService {
    @Autowired
    TourRepository tourRepository;
    @Autowired
    TourBlogRepository tourBlogRepository;
    @Autowired
    BlogRepository blogRepository;
    public List<ListTourResDTO> getTourDTOListGroupByType(){
        List<ListTourResDTO> listTourResDTOS = new ArrayList<>();
        List<TourResDTO> list = new ArrayList<>();

        ListTourResDTO dto1 = new ListTourResDTO();
        dto1.setType("Popular");
        dto1.setTours(tourRepository.findByTourType(ETourType.TYPE_POPULAR)
                .stream().map(
                        tour -> {
                            TourResMapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourResDTO dto2 = new ListTourResDTO();
        dto2.setType("Normal");
        dto2.setTours(tourRepository.findByTourType(ETourType.TYPE_NORMAL)
                .stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourResDTO dto3 = new ListTourResDTO();
        dto3.setType("Special");
        dto3.setTours(tourRepository.findByTourType(ETourType.TYPE_SPECIAL)
                .stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourResDTO dto4 = new ListTourResDTO();
        dto4.setType("Saving");
        dto4.setTours(tourRepository.findByTourType(ETourType.TYPE_SAVING)
                .stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        listTourResDTOS.add(dto1);
        listTourResDTOS.add(dto2);
        listTourResDTOS.add(dto3);
        listTourResDTOS.add(dto4);

        return listTourResDTOS;
    }

    public Map<String, Object> getAllTour(int page, int limit, int pages){
        Map<String, Object> result = new HashMap<>();
        result.put("tours", tourRepository.findAll(PageRequest.of(page, limit)).stream().map(tour -> {
            Mapper tourMapper = new TourResMapper();
            return (TourResDTO) tourMapper.mapToDTO(tour);
        }).toList());
        result.put("pages", pages == 0 ? 1 : pages);
        return result;
    }

    public Tour findTourById(Long id){
        return tourRepository.findById(id).orElse(null);
    }

    public List<TourResDTO> findTourByType(String type){
        switch (type){
            case "normal" -> {
                return tourRepository.findByTourType(ETourType.TYPE_NORMAL).stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList();
            }
            case "popular" -> {
                return tourRepository.findByTourType(ETourType.TYPE_POPULAR).stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList();
            }
            case "special" -> {
                return tourRepository.findByTourType(ETourType.TYPE_SPECIAL).stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList();
            }
            case "saving" -> {
                return tourRepository.findByTourType(ETourType.TYPE_SAVING).stream().map(
                        tour -> {
                            Mapper mapper = new TourResMapper();
                            return (TourResDTO) mapper.mapToDTO(tour);
                        }
                ).toList();
            }
            default -> {
                return null;
            }
        }
    }

    public long getCount(){
        return tourRepository.count();
    }
    public Object getTourById(Long id){
        Tour tour = tourRepository.findById(id).orElse(new Tour());
        TourBlog tourBlog = tourBlogRepository.findByTour(tour).orElse(new TourBlog());
        Blog blog = tourBlog.getBlog();
        Map<String, Object> result = new HashMap<>();
        List<Paragraph> paragraphs = (List<Paragraph>) blog.getParagraphs();

        result.put("blog", blog);
        result.put("tour", tour);
        result.put("tourBlog", tourBlog);
        result.put("paragraphs", paragraphs);
        Mapper mapper = new TourDetailResMapper();
        return (TourDetailResDTO) mapper.mapToDTO(result);
    }
}
