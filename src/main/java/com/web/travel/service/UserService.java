package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.UserUpdateReqDTO;
import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.dto.response.UserResDTO;
import com.web.travel.mapper.response.UserDetailResMapper;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EUserStatus;
import com.web.travel.repository.UserRepository;
import com.web.travel.service.interfaces.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileUploadService fileUploadService;
    public UserByEmailResDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ?
                new UserByEmailResDTO(user.getFullName(), user.getEmail()) :
                null;
    }
    public User getUserObjectByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
    public ResDTO updateUserInfo(Principal principal, UserUpdateReqDTO userDto){
        if(principal != null){
            User foundUser = getUserObjectByEmail(principal.getName());
            foundUser.setAddress(userDto.getAddress());
            foundUser.setFullName(userDto.getFullName());
            foundUser.setPhone(userDto.getPhone());

            UserDetailResMapper mapper = new UserDetailResMapper();
            UserResDTO response = (UserResDTO) mapper.mapToDTO(userRepository.save(foundUser));
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Cập nhật thông tin thành công!",
                    response
            );
        }
        return new ResDTO(
            HttpServletResponse.SC_BAD_REQUEST,
            false,
            "Không tìm thấy tài khoản người dùng!",
            null
        );
    }

    public ResDTO updateUserStatus(Principal principal, boolean status){
        if(principal != null){
            User foundUser = getUserObjectByEmail(principal.getName());
            EUserStatus userStatus = status ? EUserStatus.STATUS_ACTIVATED : EUserStatus.STATUS_NOT_ACTIVATED;
            foundUser.setActive(userStatus);

            UserDetailResMapper mapper = new UserDetailResMapper();
            UserResDTO response = (UserResDTO) mapper.mapToDTO(userRepository.save(foundUser));
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    status ? "Mở tài khoản thành công" : "Vô hiệu hóa tài khoản thành công!",
                    response
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy người dùng!",
                null
        );
    }

    public ResDTO updateUserAvatar(Principal principal, MultipartFile avatar){
        if(principal != null){
            String avatarName = null;
            try {
                avatarName = fileUploadService.uploadFile(avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }

            User foundUser = getUserObjectByEmail(principal.getName());
            foundUser.setAvatar(avatarName);
            User savedUser = userRepository.save(foundUser);
            Map<String, String> response = new HashMap<>();
            response.put("avatar", savedUser.getAvatar());
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Cập nhật ảnh đai diện thành công!",
                    response
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy người dùng!",
                null
        );
    }
}
