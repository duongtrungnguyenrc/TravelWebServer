package com.web.travel.controller;

import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.model.Order;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.service.OrderService;
import com.web.travel.service.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    OrderService orderService;
    @Autowired
    EmailService emailService;
    @Value("${travel.app.client.host}")
    String clientHost;
    @PostMapping("/create_payment")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createPayment(Principal principal, HttpServletRequest request, @RequestBody OrderReqDTO body) throws UnsupportedEncodingException {
        return ResponseEntity.ok(
                orderService.createPayment(principal, request, body)
        );
    }

    //TODO: Waiting for thank you page url from client and change vnp_ReturnUrl to it
    //TODO: Client will send the vnp_ResponseCode to update the order status
    @GetMapping("/return/{orderId}/{sessionToken}/{tourId}/{tourDateId}")
    @CrossOrigin(origins = "*")
    public Object thankYou(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @PathVariable("orderId") long orderId,
            @PathVariable("sessionToken") String sessionToken,
            @PathVariable("tourId") long tourId,
            @PathVariable("tourDateId") long tourDateId
    ){
        //Success payment
        Order order = orderService.getById(orderId);
        boolean status = true;
        if(responseCode.equals("00") || responseCode.equals("07")){
            order.setStatus(EOrderStatus.STATUS_ORDERED);
            emailService.sendOrderedEmail(order, true);
        }else{
            order.setStatus(EOrderStatus.STATUS_PENDING);
            status = false;
        }
        orderService.saveOrder(order);

        String url = clientHost + "/booking/" + tourId + "?dateId=" + tourDateId + "&orderId=" + orderId + "&status=" + status + "&sessionToken=" + sessionToken;
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url)).build();
    }
}
