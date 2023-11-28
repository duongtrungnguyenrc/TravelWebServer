package com.web.travel.mapper.request;

import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Order;
import com.web.travel.model.Tour;
import com.web.travel.model.TourDate;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.model.enumeration.EPaymentMethod;
import com.web.travel.repository.TourDateRepository;
import com.web.travel.repository.TourRepository;
import com.web.travel.repository.UserRepository;
import com.web.travel.utils.DateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderReqMapper implements Mapper {
    @Autowired
    TourRepository tourRepository;
    @Autowired
    TourDateRepository tourDateRepository;

    @Override
    public Object mapToDTO(Object obj) {
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        OrderReqDTO orderReqDTO = new OrderReqDTO();
        if(obj instanceof OrderReqDTO){
            orderReqDTO = (OrderReqDTO) obj;
        }
        Order order = new Order();
        order.setAdults(orderReqDTO.getAdults());
        order.setChildren(orderReqDTO.getChildren());
        order.setContactInfo(orderReqDTO.getContactInfo());
        order.setSpecialRequest(orderReqDTO.getSpecialRequest());
        order.setOrderDate(DateHandler.getCurrentDateTime());

        tourDateRepository.findById(orderReqDTO.getTourDateId()).ifPresent(
            tourDate -> {
                order.setTourDate(tourDate);
                long tourId = tourDate.getTour().getId();
                Tour tour = tourRepository.findById(tourId).orElseGet(Tour::new);
                order.setTour(tour);
            }
        );

        order.setPaymentMethod(EPaymentMethod.valueOf("METHOD_" + orderReqDTO.getPaymentMethod().toUpperCase()));
        order.setTotalPrice(0);
        order.setStatus(EOrderStatus.STATUS_PENDING);
        return order;
    }
}
