package com.web.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String customerFullName;
    @NotBlank
    private String customerPhone;
    @Email
    @NotBlank
    private String customerEmail;
    @NotBlank
    private String customerAddress;
    private String customerNation;

    public ContactInfo(String customerFullName, String customerPhone, String customerEmail, String customerAddress, String customerNation) {
        this.customerFullName = customerFullName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.customerNation = customerNation;
    }
}
