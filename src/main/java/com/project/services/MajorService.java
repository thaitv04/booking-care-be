package com.project.services;

import com.project.dto.MajorDTO;
import com.project.responses.MajorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MajorService {
    void createMajor(MajorDTO majorDTO, MultipartFile multipartFile) throws Exception;
    void updateMajor(MajorDTO majorDTO, MultipartFile multipartFile) throws Exception;
    List<MajorResponse> getAllMajors(String name, Integer minDoctors, Integer maxDoctors) throws Exception;
    MajorResponse getMajorById(Long id);
}
