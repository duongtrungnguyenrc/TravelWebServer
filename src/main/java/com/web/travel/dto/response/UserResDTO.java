package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.Role;
import com.web.travel.model.enumeration.ERole;
import com.web.travel.model.enumeration.EUserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResDTO {
    private Long id;
    private String email;
    private String avatar;
    private String address;
    private String fullName;
    private String phone;
    private boolean active;
    private List<String> roles;
}
