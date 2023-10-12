package com.web.travel.payload.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String newPassword;
}
