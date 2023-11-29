package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.dto.request.common.OrderUpdateReqDTO;
import com.web.travel.dto.response.OrderDetailResDTO;
import com.web.travel.mapper.request.OrderReqMapper;
import com.web.travel.mapper.response.OrderResMapper;
import com.web.travel.model.ContactInfo;
import com.web.travel.model.Order;
import com.web.travel.model.TourDate;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.repository.OrderRepository;
import com.web.travel.repository.TourDateRepository;
import com.web.travel.service.email.EmailService;
import com.web.travel.service.vnpay.VnPayService;
import com.web.travel.utils.DateHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;
    @Autowired
    TourDateRepository tourDateRepository;
    @Autowired
    VnPayService vnPayService;
    @Autowired
    OrderReqMapper orderReqMapper;
    @Autowired
    ContactInfoService contactInfoService;
    @Autowired
    TourService tourService;

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }
    public List<Order> getAll(){
        return orderRepository.findAll();
    }
    public Order getById(Long id){
        return orderRepository.findById(id).orElse(null);
    }
    public List<OrderDetailResDTO> getByUser(String userEmail){
        User user = userService.getUserObjectByEmail(userEmail);
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

        return orders.stream().map(order -> {
            OrderResMapper mapper = new OrderResMapper();
            return (OrderDetailResDTO) mapper.mapToDTO(order);
        }).collect(Collectors.toList());
    }

    public ResDTO createPayment(Principal principal, HttpServletRequest request, @RequestBody OrderReqDTO body) throws UnsupportedEncodingException {
        long amount = (long) Math.round(body.getAmount());
        String ipAddress = request.getRemoteAddr();

        Order order = (Order) orderReqMapper.mapToObject(body);
        order.setTotalPrice(body.getAmount());

        TourDate orderTourDate = order.getTourDate();
        if(orderTourDate != null){
            if(orderTourDate.getDepartDate().getTime() >= order.getOrderDate().getTime()) {
                if (orderTourDate.canBook(order.getAdults())) {
                    if (principal != null)
                        order.setUser(userService.getUserObjectByEmail(principal.getName()));

                    int currentPeople = orderTourDate.getCurrentPeople();
                    orderTourDate.setCurrentPeople(currentPeople + order.getTotalPeople());

                    tourDateRepository.save(orderTourDate);

                    ContactInfo contactInfo = contactInfoService.saveContactInfo(order.getContactInfo());
                    Long contactInfoId = contactInfo.getId();
                    order.getContactInfo().setId(contactInfoId);

                    Order savedOrder = saveOrder(order);

                    long orderId = savedOrder.getId();
                    ResDTO response = new ResDTO(
                            HttpServletResponse.SC_OK,
                            true,
                            "Đặt tour thành công!",
                            null
                    );

                    if (body.getPaymentMethod().equals("vnpay")) {
                        response = vnPayService.createPayment(amount, ipAddress, orderId);
                    } else {
                        emailService.sendOrderedEmail(order, false);
                    }

                    return response;
                }
                return new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Tour đã đầy!",
                        null
                );
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Ngày này đã đi rồi!",
                    null
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Vui lòng nhập mã ngày tour!",
                null
        );
    }

    public ResDTO updateOrderStatus(OrderUpdateReqDTO dto){
        Order order = getById(dto.getId());
        if(order != null){
            EOrderStatus needUpdateStatus = EOrderStatus.valueOf("STATUS_" + dto.getStatus().toUpperCase());

            //The needed update status must be different with the exists one
            if(order.getStatus() != needUpdateStatus) {

                Map<String, Object> response = new HashMap<>();
                response.put("id", order.getId());
                response.put("status", order.getStatus());

                //Get number of days from updated date to departure date
                DateHandler dateHandler = new DateHandler();
                int diffDay = dateHandler
                        .getDiffDay(order.getTourDate().getDepartDate(), DateHandler.getCurrentDateTime());

                //Ticket booking can only be canceled 3 days before departure date
                if(needUpdateStatus == EOrderStatus.STATUS_CANCELED){
                    if(diffDay >= 3){
                        order.setStatus(needUpdateStatus);

                        //Reduce current people due to this order is canceled
                        int currentPeople = order.getTourDate().getCurrentPeople();
                        order.getTourDate().setCurrentPeople(currentPeople - order.getTotalPeople());
                        tourDateRepository.save(order.getTourDate());

                        saveOrder(order);
                        response.put("status", order.getStatus());

                        emailService.sendCanceledEmail(order);
                        return new ResDTO(
                            HttpServletResponse.SC_OK,
                            true,
                            "Hủy đặt tour thành công!",
                            response
                        );
                    }
                    return new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Chỉ có thể hủy đặt tour trước 3 ngày trở lên!",
                        response
                    );
                }
                //Ordered status can only be updated from Pending status and vice versa
                if(order.getStatus() != EOrderStatus.STATUS_CANCELED){
                    order.setStatus(needUpdateStatus);
                    saveOrder(order);
                    response.put("status", order.getStatus());

                    if(needUpdateStatus == EOrderStatus.STATUS_ORDERED){
                        emailService.sendOrderedEmail(getById(order.getId()), true);
                    }
                    return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Cập nhật trạng thái thành công!",
                        response
                    );
                }else{
                    return new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "Không thể cập nhật trạng thái!",
                            response
                    );
                }
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Trạng thái không thay đổi!",
                    null
            );
        }
        return new ResDTO(
            HttpServletResponse.SC_BAD_REQUEST,
            false,
            "Không tìm thấy đơn hàng có mã: " + dto.getId(),
            null
        );
    }
}
