package com.web.travel.payload.request;

import com.web.travel.dto.request.common.OrderReqDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePaymentRequest {
    private OrderReqDTO order;
}
