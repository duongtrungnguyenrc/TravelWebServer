package com.web.travel.service;

import com.web.travel.model.Payment;
import com.web.travel.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    public Payment savePayment(Payment payment){
        return paymentRepository.save(payment);
    }
}
