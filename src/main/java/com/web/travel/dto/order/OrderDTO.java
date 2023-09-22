package com.web.travel.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.ContactInfo;
import com.web.travel.model.Tour;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
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
