package com.web.travel.security;

import com.nimbusds.jose.shaded.gson.Gson;
import com.web.travel.dto.ResDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf8");

        ResDTO resDTO = new ResDTO();
        resDTO.setCode(HttpServletResponse.SC_FORBIDDEN);
        resDTO.setStatus(false);

        Map<String, String> data = new HashMap<>();
        data.put("path", request.getServletPath());
        data.put("error", "Access denied");
        resDTO.setData(data);

        resDTO.setMessage("You do not have permission to access this resource!");

        Gson gson = new Gson();
        String responseContent = gson.toJson(resDTO);

        response.getWriter().write(responseContent);
    }
}
