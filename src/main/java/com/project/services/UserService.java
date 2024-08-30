package com.project.services;

import com.project.dto.UserDTO;
import com.project.requests.UserPasswordRequest;
import com.project.requests.UserSearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, String> login(String username, String password) throws Exception;
    void createUser(UserDTO userDTO, MultipartFile multipartFile) throws Exception;
    void updateUser(UserDTO userDTO, MultipartFile multipartFile) throws Exception;
    UserDTO getUserById(Long id) throws Exception;
    List<UserDTO> getAllUsers(UserSearchRequest request);
    void deleteUserById(Long id) throws Exception;
    String changePassword(UserPasswordRequest userPasswordRequest) throws Exception;
}
