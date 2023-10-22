package com.web.travel.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmCodeRequest {
    private String email;
    private String token;
    private String confirmCode;
}