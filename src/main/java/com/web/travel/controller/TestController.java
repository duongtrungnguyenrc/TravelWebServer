package com.web.travel.controller;

import com.web.travel.model.Order;
import com.web.travel.payload.response.AuthResponse;
import com.web.travel.repository.OrderRepository;
import com.web.travel.service.FileUploadService;
import com.web.travel.service.cloudinary.EStatus;
import com.web.travel.service.cloudinary.FilesValidation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private FilesValidation filesValidation;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    OrderRepository orderRepository;
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/tour")
    public String tourAccess() {
        return "Tour Management Board.";
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @PostMapping("/add/order")
    public String addOrder(@Valid @RequestBody Order order){
        orderRepository.save(order);
        return "oK";
    }
    @PostMapping("/upload")
    public ResponseEntity<AuthResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        EStatus eStatus = filesValidation.validate(files);
        switch (eStatus){
            case STATUS_EMPTY_FILE -> {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(HttpServletResponse.SC_BAD_REQUEST, "Lack of images")
                );
            }
            case STATUS_WRONG_EXT -> {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(HttpServletResponse.SC_BAD_REQUEST, "Format is not supported")
                );
            }
            default -> {
                List<String> fileNames = fileUploadService.uploadMultiFile(files);
                return ResponseEntity.ok(
                        new AuthResponse(HttpServletResponse.SC_OK, fileNames)
                );
            }
        }
    }
}