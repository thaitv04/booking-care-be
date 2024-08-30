package com.project.services.impl;

import com.cloudinary.Cloudinary;
import com.project.exceptions.FuncErrorException;
import com.project.responses.CloudinaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl {
    private final Cloudinary cloudinary;

    public Map upload(MultipartFile multipartFile) throws IOException {
        // Sử dụng một HashMap rỗng thay cho Map.of()
        Map<String, Object> emptyMap = new HashMap<>();
        Map data = this.cloudinary.uploader().upload(multipartFile.getBytes(), emptyMap);
        return data;
    }

    public Map delete(String id) throws IOException {
        Map<String, Object> emptyMap = new HashMap<>();
        Map data = this.cloudinary.uploader().destroy(id, emptyMap);
        return data;
    }
}
