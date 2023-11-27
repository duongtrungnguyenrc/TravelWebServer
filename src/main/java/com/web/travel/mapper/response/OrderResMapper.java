package com.web.travel.mapper.response;

import com.web.travel.dto.response.OrderDetailResDTO;
import com.web.travel.dto.response.TourResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Order;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.model.enumeration.EPaymentMethod;
import com.web.travel.utils.DateHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        Order order = (Order) obj;
        OrderDetailResDTO dto = new OrderDetailResDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setAdults(order.getAdults());
        dto.setChildren(order.getChildren());

        TourResMapper tourMapper = new TourResMapper();
        TourResDTO tourDto = (TourResDTO) tourMapper.mapToDTO(order.getTour());
        DateHandler dateHandler = new DateHandler();
        tourDto.setDuration(dateHandler.getDiffDay(
                order.getTourDate().getEndDate(), order.getTourDate().getDepartDate()));
        dto.setTour(tourDto);

        if(order.getStatus() == EOrderStatus.STATUS_CANCELED)
            dto.setStatus("Đã hủy");
        else if(order.getStatus() == EOrderStatus.STATUS_ORDERED)
            dto.setStatus("Đã thanh toán");
        else
            dto.setStatus("Đang chờ");

        dto.setContactInfo(order.getContactInfo());
        dto.setDepartDate(order.getTourDate().getDepartDate());
        dto.setEndDate(order.getTourDate().getEndDate());

        if(order.getPaymentMethod() == EPaymentMethod.METHOD_VNPAY)
            dto.setPaymentMethod("Thanh toán bằng VNPay");
        else
            dto.setPaymentMethod("Thanh toán bằng tiền mặt");

        dto.setTotalPrice(order.getTotalPrice());

        return dto;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}