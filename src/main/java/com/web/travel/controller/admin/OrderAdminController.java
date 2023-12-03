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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
@CrossOrigin(origins = "*")

public class OrderAdminController {
    @Autowired
    OrderService orderService;
    @Autowired
    EmailService emailService;
    @PostMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateOrderStatus(Principal principal,  @RequestBody OrderUpdateReqDTO orderDto){
        ResDTO response = orderService.updateOrderStatus(principal, false, orderDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrder(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        return ResponseEntity.ok().body(
                new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Orders fetched successfully!",
                    orderService.getAllResponse(page, limit)
                )
        );
    }
}
