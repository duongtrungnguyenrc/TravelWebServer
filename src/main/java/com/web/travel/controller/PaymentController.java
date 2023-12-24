package com.web.travel.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.web.travel.dto.request.common.OrderReqDTO;
import com.web.travel.model.Order;
import com.web.travel.model.enums.EOrderStatus;
import com.web.travel.service.OrderService;
import com.web.travel.service.email.EmailService;
import com.web.travel.service.paypal.PaypalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    OrderService orderService;
    @Autowired
    EmailService emailService;
    @Autowired
    PaypalService paypalService;
    @Value("${travel.app.client.host}")
    String clientHost;
    @PostMapping("/create_payment")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createPayment(Principal principal, HttpServletRequest request, @RequestBody OrderReqDTO body) throws UnsupportedEncodingException {
        return ResponseEntity.ok(
                orderService.createPayment(principal, request, body, false)
        );
    }

    @PostMapping("/app/create_payment")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createAppPayment(Principal principal, HttpServletRequest request, @RequestBody OrderReqDTO body) throws UnsupportedEncodingException {
        return ResponseEntity.ok(
                orderService.createPayment(principal, request, body, true)
        );
    }

    //TODO: Waiting for thank you page url from client and change vnp_ReturnUrl to it
    //TODO: Client will send the vnp_ResponseCode to update the order status
    @GetMapping("/return/{orderId}/{sessionToken}/{tourId}/{tourDateId}/{isApp}")
    @CrossOrigin(origins = "*")
    public Object thankYou(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @PathVariable("orderId") long orderId,
            @PathVariable("sessionToken") String sessionToken,
            @PathVariable("tourId") long tourId,
            @PathVariable("tourDateId") long tourDateId,
            @PathVariable("isApp") boolean isApp,
            HttpServletResponse response
    ) throws URISyntaxException, IOException {
        //Success payment
        Order order = orderService.getById(orderId);
        boolean status = true;
        if(responseCode.equals("00") || responseCode.equals("07")){
            order.setStatus(EOrderStatus.STATUS_ORDERED);
            emailService.sendOrderedEmail(order, true);
        }else{
            order.setStatus(EOrderStatus.STATUS_CANCELED);
            emailService.sendCanceledEmail(order);
            status = false;
        }
        orderService.saveOrder(order);

        String url = clientHost + "/booking/" + tourId + "?date=" + tourDateId + "&orderId=" + orderId + "&status=" + status + "&sessionToken=" + sessionToken;
        if (isApp)
            url = "intent:#Intent;" +
                    "action=com.main.travelApp.action.PAYMENT_RESULT;" +
                    "package=com.main.travelApp;" +
                    "S.orderId="+ orderId +";" +
                    "S.status="+ status +";" +
                    "end";

        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.sendRedirect(url);

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/success/{orderId}/{sessionToken}/{tourId}/{tourDateId}/{isApp}")
    @CrossOrigin(origins = "*")
    public Object successPaypal(
            @PathVariable("orderId") long orderId,
            @PathVariable("sessionToken") String sessionToken,
            @PathVariable("tourId") long tourId,
            @PathVariable("tourDateId") long tourDateId,
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @PathVariable("isApp") boolean isApp,
            HttpServletResponse response
    ) throws URISyntaxException, IOException {
        boolean status = false;
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);

            if (payment.getState().equals("approved")) {
                Order order = orderService.getById(orderId);

                order.setStatus(EOrderStatus.STATUS_ORDERED);
                emailService.sendOrderedEmail(order, true);
                orderService.saveOrder(order);

                status = true;
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        String url = clientHost + "/booking/" + tourId + "?date=" + tourDateId + "&orderId=" + orderId + "&status=" + status + "&sessionToken=" + sessionToken;
        if (isApp)
            url = "intent:#Intent;" +
                    "action=com.main.travelApp.action.PAYMENT_RESULT;" +
                    "package=com.main.travelApp;" +
                    "S.orderId="+ orderId +";" +
                    "S.status="+ true +";" +
                    "end";

        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.sendRedirect(url);

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/cancel/{orderId}/{sessionToken}/{tourId}/{tourDateId}/{isApp}")
    @CrossOrigin(origins = "*")
    public Object cancelPaypal(
            @PathVariable("orderId") long orderId,
            @PathVariable("sessionToken") String sessionToken,
            @PathVariable("tourId") long tourId,
            @PathVariable("tourDateId") long tourDateId,
            @PathVariable("isApp") boolean isApp,
            HttpServletResponse response
    ) throws IOException, URISyntaxException {
        Order order = orderService.getById(orderId);

        order.setStatus(EOrderStatus.STATUS_CANCELED);
        emailService.sendCanceledEmail(order);
        orderService.saveOrder(order);

        String url = clientHost + "/booking/" + tourId + "?date=" + tourDateId + "&orderId=" + orderId + "&status=" + false + "&sessionToken=" + sessionToken;
        if(isApp)
            url = "intent:#Intent;" +
                    "action=com.main.travelApp.action.PAYMENT_RESULT;" +
                    "package=com.main.travelApp;" +
                    "S.orderId="+ orderId +";" +
                    "S.status="+ false +";" +
                    "end";

        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.sendRedirect(url);

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}
