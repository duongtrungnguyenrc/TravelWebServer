package com.web.travel.mapper;

import com.web.travel.dto.order.OrderDTO;
import com.web.travel.model.Order;
import com.web.travel.model.Tour;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.repository.TourRepository;
import com.web.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements Mapper{
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
        OrderDTO orderDTO = new OrderDTO();
        if(obj instanceof OrderDTO){
            orderDTO = (OrderDTO) obj;
        }
        Order order = new Order();
        order.setAdults(orderDTO.getAdults());
        order.setChildren(order.getChildren());
        order.setContactInfo(orderDTO.getContactInfo());
        order.setOrderDate(orderDTO.getOrderDate());
        User user = userRepository.findById(orderDTO.getUserId()).orElseGet(User::new);
        order.setUser(user);
        Tour tour = tourRepository.findById(orderDTO.getTourId()).orElseGet(Tour::new);
        order.setTour(tour);
        order.setTotalPrice(0);
        order.setStatus(EOrderStatus.STATUS_PENDING);
        return order;
    }
}
