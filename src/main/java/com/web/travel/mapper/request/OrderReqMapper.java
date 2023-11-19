package com.web.travel.mapper.request;

import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Order;
import com.web.travel.model.Tour;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.repository.TourRepository;
import com.web.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderReqMapper implements Mapper {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TourRepository tourRepository;

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
        order.setChildren(order.getChildren());
        order.setContactInfo(orderReqDTO.getContactInfo());
        order.setOrderDate(orderReqDTO.getOrderDate());
        User user = userRepository.findById(orderReqDTO.getUserId()).orElseGet(User::new);
        order.setUser(user);
        Tour tour = tourRepository.findById(orderReqDTO.getTourId()).orElseGet(Tour::new);
        order.setTour(tour);
        order.setTotalPrice(0);
        order.setStatus(EOrderStatus.STATUS_PENDING);
        return order;
    }
}
