package com.web.travel.dto.response;

import com.web.travel.model.Role;
import com.web.travel.model.enumeration.ERole;
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
    private List<ERole> roles;
}
