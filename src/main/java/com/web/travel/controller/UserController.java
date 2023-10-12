package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.UserByEmailReqDTO;
import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService service;
    @PostMapping("/get-by-email")
    public ResponseEntity<ResDTO> getUserById(@RequestBody UserByEmailReqDTO user){
        UserByEmailResDTO foundUser = service.getUserByEmail(user.getEmail());
        return foundUser != null ? ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "User found",
                        foundUser)
        ) : ResponseEntity.badRequest().body(
                new ResDTO(HttpStatus.BAD_REQUEST.value(),
                        false,
                        "User not found with email: " + user.getEmail(),
                        null)
        );
    }
}
