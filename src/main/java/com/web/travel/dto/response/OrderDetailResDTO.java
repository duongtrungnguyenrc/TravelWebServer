package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.ContactInfo;
import com.web.travel.model.Tour;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.model.enumeration.EPaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResDTO {
    private long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date orderDate;
    private int adults;
    private int children;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date departDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date endDate;
    private String paymentMethod;
    private String status;
    private double totalPrice;
    private TourResDTO tour;
    private ContactInfo contactInfo;
}
