package com.web.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderReqDTO {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date orderDate = new Date();
    private int adults;
    private int children;
    private String paymentMethod;
    @JsonIgnore
    private double totalPrice;
    private Long userId;
    private Long tourId;
    private ContactInfo contactInfo;
}
