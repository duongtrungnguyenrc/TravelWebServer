package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.OrderUpdateReqDTO;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.service.OrderService;
import com.web.travel.service.email.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Http2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
public class OrderAdminController {
    @Autowired
    OrderService orderService;
    @Autowired
    EmailService emailService;
    @PostMapping("/update")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderUpdateReqDTO orderDto){
        ResDTO response = orderService.updateOrderStatus(orderDto);
        return ResponseEntity.ok().body(response);
    }
}
