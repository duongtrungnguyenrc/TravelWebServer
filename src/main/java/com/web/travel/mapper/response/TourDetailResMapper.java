package com.web.travel.mapper.response;

import com.web.travel.dto.response.*;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TourDetailResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        if(obj instanceof Map<?,?>){
            Tour tour = (Tour) ((Map<?, ?>) obj).get("tour");
            TourBlog tourBlog = (TourBlog) ((Map<?, ?>) obj).get("tourBlog");
            Blog blog = (Blog) ((Map<?, ?>) obj).get("blog");
            List<Paragraph> paragraphs = (List<Paragraph>) ((Map<?, ?>) obj).get("paragraphs");
            TourDetailResDTO tourDto = new TourDetailResDTO();
            tourDto.setId(tour.getId());
            tourDto.setName(tour.getName());
            tourDto.setTourType(tour.getTourType());
            tourDto.setAdultPrice(tour.getAdultPrice());
            tourDto.setChildPrice(tour.getChildPrice());
            tourDto.setVehicle(tour.getVehicle());
            tourDto.setDepart(tour.getDepart());
            tourDto.setDestination(tour.getDestination());
            tourDto.setDepartDate(tour.getDepartDate());
            tourDto.setEndDate(tour.getEndDate());
            tourDto.setMaxPeople(tour.getMaxPeople());
            tourDto.setCurrentPeople(tour.getCurrentPeople());
            tourDto.setImg(tour.getImg());

            List<ParagraphResDTO> paragraphDTO = new ArrayList<ParagraphResDTO>();
            paragraphs.forEach(paragraph -> {
                ParagraphResDTO paragraphResDTO = new ParagraphResDTO();
                paragraphResDTO.setId(paragraph.getId());
                paragraphResDTO.setContent(paragraph.getContent());
                List<ParagraphImageResDTO> images = new ArrayList<>();
                paragraph.getParagraphImgs().forEach(image -> {
                    ParagraphImageResDTO paragraphImageResDTO = new ParagraphImageResDTO();
                    paragraphImageResDTO.setImg(image.getImg());
                    paragraphImageResDTO.setName(image.getName());
                    images.add(paragraphImageResDTO);
                });
                paragraphResDTO.setImages(images);
                paragraphDTO.add(paragraphResDTO);
            });

            TourBlogResDTO tourBlogResDTO = new TourBlogResDTO();
            tourBlogResDTO.setId(tourBlog.getId());
            tourBlogResDTO.setParagraphs(paragraphDTO);
            tourBlogResDTO.setBackgroundImage(blog.getBackgroundImg());

            tourDto.setBlog(tourBlogResDTO);
            List<HotelResDTO> hotels = new ArrayList<>();
            tour.getHotels().forEach(hotel -> {
                HotelResDTO hotelResDTO = new HotelResDTO();
                hotelResDTO.setId(hotel.getId());
                hotelResDTO.setName(hotel.getName());
                hotelResDTO.setAddress(hotel.getAddress());
                List<RoomResDTO> roomResDTOS = new ArrayList<>();
                hotel.getRooms().forEach(
                    room -> {
                        RoomResDTO roomResDTO = new RoomResDTO();
                        roomResDTO.setId(room.getId());
                        roomResDTO.setType(room.getType().toString());
                        roomResDTO.setPrice(room.getPrice());
                        roomResDTOS.add(roomResDTO);
                    }
                );
                hotelResDTO.setRooms(roomResDTOS);

                hotels.add(hotelResDTO);
            });
            tourDto.setHotels(hotels);

            return tourDto;
        }
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
