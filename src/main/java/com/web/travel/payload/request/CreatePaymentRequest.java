package com.web.travel.payload.request;

import com.web.travel.dto.request.OrderReqDTO;
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
    private OrderReqDTO order;
}
