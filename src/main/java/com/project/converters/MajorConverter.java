package com.project.converters;

import com.project.dto.UserDTO;
import com.project.models.Major;
import com.project.models.User;
import com.project.repositories.MajorRepository;
import com.project.responses.MajorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class MajorConverter {
    private final ModelMapper modelMapper;
    private final MajorRepository majorRepository;

    /**
     * Phương thức này để chuyển đổi từ UserDTO sang UserEntity
     * @param major - dưới dạng Entity
     * @return dưới dạng Response
     */
    public MajorResponse fromMajorToMajorResponse(Major major) {
        MajorResponse majorResponse = modelMapper.map(major, MajorResponse.class);
        Integer countDoctor = major.getUserEntities().size();
        majorResponse.setNumberOfDoctor(countDoctor);
        return majorResponse;
    }
}
