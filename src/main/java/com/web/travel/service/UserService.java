package com.web.travel.service;

import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.model.User;
import com.web.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserByEmailResDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ?
                new UserByEmailResDTO(user.getFullName(), user.getEmail()) :
                null;
    }

    public User getUserObjectByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
}
