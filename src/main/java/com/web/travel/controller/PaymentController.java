package com.web.travel.controller;

import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.model.Order;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.service.OrderService;
import com.web.travel.service.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    OrderService orderService;
    @Autowired
    EmailService emailService;
    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(Principal principal, HttpServletRequest request, @RequestBody OrderReqDTO body) throws UnsupportedEncodingException {
        return ResponseEntity.ok(
                orderService.createPayment(principal, request, body)
        );
    }

    //TODO: Waiting for thank you page url from client and change vnp_ReturnUrl to it
    //TODO: Client will send the vnp_ResponseCode to update the order status
    @GetMapping("/return/{orderId}")
    public String thankYou(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @PathVariable("orderId") long orderId){
        //Success payment
        Order order = orderService.getById(orderId);
        if(responseCode.equals("00") || responseCode.equals("07")){
            order.setStatus(EOrderStatus.STATUS_ORDERED);
            emailService.sendOrderedEmail(order, true);
        }else{
            order.setStatus(EOrderStatus.STATUS_PENDING);
        }
        orderService.saveOrder(order);
        return "thank you";
    }
}
