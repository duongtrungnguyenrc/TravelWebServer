package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.enumeration.EPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date paymentDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private EPaymentStatus status;
    @OneToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
