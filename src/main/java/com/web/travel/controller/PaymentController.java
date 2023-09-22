package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.mapper.OrderMapper;
import com.web.travel.model.ContactInfo;
import com.web.travel.model.Order;
import com.web.travel.model.Payment;
import com.web.travel.model.enumeration.EPaymentStatus;
import com.web.travel.payload.request.CreatePaymentRequest;
import com.web.travel.service.ContactInfoService;
import com.web.travel.service.OrderService;
import com.web.travel.service.PaymentService;
import com.web.travel.service.TourService;
import com.web.travel.service.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    VnPayService vnPayService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    PaymentService paymentService;
    @Autowired
    OrderService orderService;
    @Autowired
    ContactInfoService contactInfoService;
    @Autowired
    TourService tourService;
    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody CreatePaymentRequest body) throws UnsupportedEncodingException {
        long amount = (long) Math.round(body.getAmount());
        String ipAddress = request.getRemoteAddr();

        Order order = (Order) orderMapper.mapToObject(body.getOrder());
        order.setTotalPrice(body.getAmount());

        ContactInfo contactInfo = contactInfoService.saveContactInfo(order.getContactInfo());
        Long contactInfoId = contactInfo.getId();
        order.getContactInfo().setId(contactInfoId);
        order.setTour(tourService.findTourById(
                body.getOrder().getTourId()
        ));
        Order savedOrder= orderService.saveOrder(order);

        long orderId = savedOrder.getId();
        ResDTO response = vnPayService.createPayment(amount, ipAddress, body.getMethod(), orderId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/return/{orderId}")
    public String thankyou(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_BankCode") String method,
            @PathVariable("orderId") long orderId){
        //Success payment
        Payment payment = new Payment();
        long paymentAmount = Long.parseLong(amount);
        payment.setAmount(paymentAmount);
        payment.setPaymentDate(new Date());
        payment.setMethod(method);
        Order order = orderService.getById(orderId);
        payment.setOrder(order);
        if(responseCode.equals("00") || responseCode.equals("07")){
            payment.setStatus(EPaymentStatus.STATUS_SUCCESS);
        }else if (responseCode.equals("24")){
            payment.setStatus(EPaymentStatus.STATUS_CANCELLED);
        }else{
            payment.setStatus(EPaymentStatus.STATUS_FAILED);
        }
        paymentService.savePayment(payment);
        return "thank you";
    }
}
