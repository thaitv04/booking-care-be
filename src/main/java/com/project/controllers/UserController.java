package com.project.controllers;

import com.project.dto.UserDTO;
import com.project.requests.UserPasswordRequest;
import com.project.requests.UserSearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/search")
    public ResponseEntity<?> getAllUsers(@RequestBody UserSearchRequest request) {
        try{
            List<UserDTO> users = userService.getAllUsers(request);
            return ResponseEntity.ok().body(users);
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDTO existingUser = userService.getUserById(id);
            return ResponseEntity.ok().body(existingUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestPart("file") MultipartFile multipartFile,
                                        @RequestParam("userdto") String userdtoJson) {
        try{
            UserDTO userDTO = objectMapper.readValue(userdtoJson, UserDTO.class);
            userService.createUser(userDTO, multipartFile);
            return ResponseEntity.ok().body("Thành công");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); //rule 5
        }
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserPasswordRequest userPasswordRequest) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userPasswordRequest.setUsername(authentication.getName());
            String message = userService.changePassword(userPasswordRequest);
            return ResponseEntity.ok().body(message);
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestPart("file") MultipartFile multipartFile,
                                        @RequestParam("userdto") String userdtoJson) {
        try{
            UserDTO userDTO = objectMapper.readValue(userdtoJson, UserDTO.class);
            userService.updateUser(userDTO, multipartFile);
            return ResponseEntity.ok().body("Thành công");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); //rule 5
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok().body("Thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
