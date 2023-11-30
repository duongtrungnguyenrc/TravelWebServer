package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.UserUpdateReqDTO;
import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.dto.response.UserResDTO;
import com.web.travel.mapper.response.UserDetailResMapper;
import com.web.travel.model.Role;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.ERole;
import com.web.travel.model.enumeration.EUserStatus;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.MessageResponse;
import com.web.travel.repository.RoleRepository;
import com.web.travel.repository.UserRepository;
import com.web.travel.service.interfaces.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FileUploadService fileUploadService;
    public UserByEmailResDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ?
                new UserByEmailResDTO(user.getFullName(), user.getEmail()) :
                null;
    }
    public boolean userIsExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User saveDefaultUser(SignupRequest signUpRequest){
        // Create new user's account
        User user = new User(signUpRequest.getFullName(),
                signUpRequest.getAddress(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getPhone());

        user.setAvatar(signUpRequest.getAvatar());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role userRole = roleRepository.findByName(ERole.valueOf("ROLE_" + role.toUpperCase()))
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                roles.add(userRole);
            });
        }

        user.setRoles(roles);
        user.setActive(EUserStatus.STATUS_ACTIVATED);

        return userRepository.save(user);
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
