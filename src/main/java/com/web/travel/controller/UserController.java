package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.UserByEmailReqDTO;
import com.web.travel.dto.request.common.UserUpdateReqDTO;
import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
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

    @PostMapping("/update")
    private ResponseEntity<?> updateUserInfo(Principal principal, @RequestBody UserUpdateReqDTO userDto){
        ResDTO response = service.updateUserInfo(principal, userDto);

        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/status/{status}")
    private ResponseEntity<?> updateUserStatus(Principal principal, @PathVariable boolean status){
        ResDTO response = service.updateUserStatus(principal, status);
        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/avatar")
    private ResponseEntity<?> updateUserAvatar(Principal principal, @RequestParam MultipartFile avatar){
        ResDTO response = service.updateUserAvatar(principal, avatar);
        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
