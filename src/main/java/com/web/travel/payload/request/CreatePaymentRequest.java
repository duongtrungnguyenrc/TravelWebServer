package com.web.travel.payload.request;

import com.web.travel.dto.order.OrderDTO;
import com.web.travel.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePaymentRequest {
//    private long orderId;
    private double amount;
    private String method;
    private OrderDTO order;
}
