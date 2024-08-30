package com.project.converters;

import com.project.dto.UserDTO;
import com.project.models.User;
import com.project.repositories.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ModelMapper modelMapper;
    private final MajorRepository majorRepository;

    /**
     * Phương thức này để chuyển đổi từ UserDTO sang UserEntity
     *
     * @param userDTO - dưới dạng DTO
     * @return dưới dạng Entity
     */
    public User fromDTOtoUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setMajorUserEntities(majorRepository.findById(userDTO.getMajorId()).get());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfBirth = LocalDate.parse(userDTO.getDateOfBirth(), formatter);
        user.setDateOfBirth(dateOfBirth);
        return user;
    }

    /**
     * Phương thức này dùng để chuyển đổi từ UserEntity thành UserDTO
     * @param user - Dạng entity
     * @return UserDTO
     */
    public UserDTO fromUserToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setMajorId(user.getMajorUserEntities().getId());
        userDTO.setMajorName(user.getMajorUserEntities().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        userDTO.setDateOfBirth(user.getDateOfBirth().format(formatter));
        return userDTO;
    }

    /**
     * Phương thức sau để cập nhật 1 User
     *
     * @param userDTO - DTO dùng để sao chép
     * @param user    - User cần sao chép
     */
    public void updateUserFromDTO(UserDTO userDTO, User user) {
        modelMapper.map(userDTO, user);

        if (userDTO.getMajorId() != null) {
            Long majorId = userDTO.getMajorId();
            user.setMajorUserEntities(majorRepository.findById(majorId).orElse(null));
        }

        if (userDTO.getDateOfBirth() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateOfBirth = LocalDate.parse(userDTO.getDateOfBirth(), formatter);
            user.setDateOfBirth(dateOfBirth);
        }
    }

}
