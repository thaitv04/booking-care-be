package com.project.services.impl;

import com.project.components.JwtTokenUtil;
import com.project.constants.SystemConstant;
import com.project.converters.UserConverter;
import com.project.dto.UserDTO;
import com.project.exceptions.DataNotFoundException;
import com.project.models.User;
import com.project.repositories.RoleRepository;
import com.project.repositories.UserRepository;
import com.project.requests.UserPasswordRequest;
import com.project.requests.UserSearchRequest;
import com.project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final UserConverter userConverter;

    @Override
    public Map<String, String> login(String username, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new DataNotFoundException("Tài khoản không tồn tại");
        }
        User existingUser = optionalUser.get();
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password,
                existingUser.getAuthorities()
        );
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtil.generateToken(existingUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("role", existingUser.getAuthorities().iterator().next().getAuthority());
        map.put("userId", String.valueOf(existingUser.getId()));
        return map;
    }

    @Override
    public UserDTO getUserById(Long id) throws Exception {
        User user = userRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException("Cannot find user with id " + id));
        return userConverter.fromUserToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers(UserSearchRequest request) {
        return userRepository.getAllUsers(request).stream()
                .map(userConverter::fromUserToDTO) //Chuyển từng User thành UserDTO
                .collect(Collectors.toList()); //Tạo List mới
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find user with id " + id));
        user.setStatus(0);
        userRepository.save(user);
    }

    @Override
    public String changePassword(UserPasswordRequest userPasswordRequest) throws Exception {
        Optional<User> user = userRepository.findByUsername(userPasswordRequest.getUsername());
        if (!user.isPresent()) {
            throw new DataNotFoundException("Cannot find user with name " + userPasswordRequest.getUsername());
        } else if (!passwordEncoder.matches(userPasswordRequest.getPassword(), user.get().getPassword())) {
            return "Mật khẩu không chính xác";
        } else if (!userPasswordRequest.getNewPassword().equals(userPasswordRequest.getRetypePassword())) {
            return "Nhập lại mật khẩu không khớp";
        } else {
            user.get().setPassword(passwordEncoder.encode(userPasswordRequest.getNewPassword()));
            userRepository.save(user.get());
            return "Thành công";
        }
    }

    @Override
    @Transactional
    public void createUser(UserDTO userDTO, MultipartFile multipartFile) throws Exception {
        //Check tài khoản tồn tại chưa
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Tài khoản đã tồn tại");
        }

        //Thêm user
        User newUser = userConverter.fromDTOtoUser(userDTO);
        //Upload ảnh
        if (multipartFile != null && !multipartFile.isEmpty()) {
            Map<String, Object> data = cloudinaryService.upload(multipartFile);
            newUser.setIdimage(data.get("public_id").toString());
            newUser.setAvatar(data.get("url").toString());
        }
        newUser.setPassword(passwordEncoder.encode(SystemConstant.DEFAULT_PASSWORD));
        newUser.setRoleUserEntities(roleRepository.findByName(SystemConstant.DEFAULT_ROLE));
        newUser.setStatus(1);
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO, MultipartFile multipartFile) throws Exception {
        //Check tài khoản tồn tại chưa
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (!optionalUser.isPresent()) {
            throw new DataNotFoundException("Tài khoản không tồn tại");
        }

        //Cập nhật user
        User updateUser = optionalUser.get();
        userConverter.updateUserFromDTO(userDTO, updateUser);
        // Upload ảnh
        if (multipartFile != null && !multipartFile.isEmpty()) {
            cloudinaryService.delete(updateUser.getIdimage());
            Map<String, Object> data = cloudinaryService.upload(multipartFile);
            updateUser.setIdimage(data.get("public_id").toString());
            updateUser.setAvatar(data.get("url").toString());
        }
        userRepository.save(updateUser);
    }

    /**
     * Phương thức này dùng để tự động cập nhật số năm kinh nghiệm của bác sĩ hằng năm
     */
    @Scheduled(cron = "0 0 0 1 1 *") // Chạy vào ngày 1 tháng 1 mỗi năm
    public void updateExperience() {
        List<User> doctors = userRepository.findAll();
        for (User doctor : doctors) {
            Integer experience = doctor.getExperience() + 1;
            doctor.setExperience(experience);
        }
        userRepository.saveAll(doctors);
    }
}
