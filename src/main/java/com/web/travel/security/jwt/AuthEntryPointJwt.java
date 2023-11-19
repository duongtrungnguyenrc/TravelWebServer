package com.web.travel.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.response.AuthResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        String message = "";
        if(authException.getMessage().equals("Bad credentials")){
            message = "Sai email hoặc mật khẩu";
        }else if (authException.getMessage().equals("Full authentication is required to access this resource")){
            message = "Chưa đăng nhập";
        }else if (authException.getMessage().equals("User account is locked")){
//            message = "Tài khoản của bạn đã bị vô hiệu hóa";
            message = authException.getMessage();
        }
        body.put("error", "Unauthorized");
        body.put("path", request.getServletPath());
        ResDTO authResponse = new ResDTO(
                HttpServletResponse.SC_UNAUTHORIZED,
                false,
                message,
                body
        );

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), authResponse);
    }
}
