package com.web.travel.security.oauth2;

import com.nimbusds.jose.shaded.gson.Gson;
import com.web.travel.mapper.response.SignInResMapper;
import com.web.travel.model.User;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.SignInResponse;
import com.web.travel.security.jwt.JwtUtils;
import com.web.travel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            String email = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";
            String imgUrl = userDetails.getAttribute("picture");
            String fullName = userDetails.getAttribute("name");
            SignInResponse signInResponse = new SignInResponse();
            if(!userService.userIsExistsByEmail(email)){
                SignupRequest dto = new SignupRequest();
                dto.setPhone("");
                dto.setEmail(email);
                dto.setPassword("default-password");
                dto.setAddress("");
                dto.setFullName(fullName);
                dto.setAvatar(imgUrl);

                Set<String> roles = new HashSet<>();
                roles.add("user");
                dto.setRole(roles);

                User savedUser = userService.saveDefaultUser(dto);

                signInResponse = (SignInResponse) new SignInResMapper().mapToDTO(savedUser);

            }else{
                User foundUser = userService.getUserObjectByEmail(email);
                signInResponse = (SignInResponse) new SignInResMapper().mapToDTO(foundUser);

            }
            String token = jwtUtils.generateJwtToken(email);
            signInResponse.setAccessToken(token);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Gson gson = new Gson();
            String json = gson.toJson(signInResponse);


            response.getWriter().write(json);
        }
    }
}
