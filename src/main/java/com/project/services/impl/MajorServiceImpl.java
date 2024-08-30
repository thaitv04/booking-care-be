package com.project.services.impl;

import com.project.converters.MajorConverter;
import com.project.dto.MajorDTO;
import com.project.exceptions.DataNotFoundException;
import com.project.models.Major;
import com.project.repositories.MajorRepository;
import com.project.responses.MajorResponse;
import com.project.services.MajorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;
    private final MajorConverter majorConverter;
    private final CloudinaryServiceImpl cloudinaryService;
    private final ModelMapper modelMapper;

    @Override
    public List<MajorResponse> getAllMajors(String name, Integer minDoctors, Integer maxDoctors) {
        return majorRepository.getAllMajors(name, minDoctors, maxDoctors).stream()
                .map(majorConverter::fromMajorToMajorResponse) //Chuyển từng Major thành MajorResponse
                .collect(Collectors.toList()); //Tạo List mới
    }

    @Override
    public MajorResponse getMajorById(Long id) {
        Major major = majorRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException("Cannot find major with id " + id));
        return majorConverter.fromMajorToMajorResponse(major);
    }

    @Override
    @Transactional
    public void createMajor(MajorDTO majorDTO, MultipartFile multipartFile) throws Exception{
        if(majorRepository.existsByName(majorDTO.getName())){
            throw new DataIntegrityViolationException("Chuyên khoa đã tồn tại");
        }

        Major newMajor = modelMapper.map(majorDTO, Major.class);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            Map<String, Object> data = cloudinaryService.upload(multipartFile);
            newMajor.setIdimage(data.get("public_id").toString());
            newMajor.setImage(data.get("url").toString());
        }
        majorRepository.save(newMajor);
    }

    @Override
    @Transactional
    public void updateMajor(MajorDTO majorDTO, MultipartFile multipartFile) throws Exception {
        Optional<Major> optionalMajor = majorRepository.findById(majorDTO.getId());
        if (!optionalMajor.isPresent()) {
            throw new DataNotFoundException("Chuyên khoa không tồn tại");
        }

        Major updateMajor = optionalMajor.get();
        modelMapper.map(majorDTO, updateMajor);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            cloudinaryService.delete(updateMajor.getIdimage());
            Map<String, Object> data = cloudinaryService.upload(multipartFile);
            updateMajor.setIdimage(data.get("public_id").toString());
            updateMajor.setImage(data.get("url").toString());
        }
        majorRepository.save(updateMajor);
    }


}
