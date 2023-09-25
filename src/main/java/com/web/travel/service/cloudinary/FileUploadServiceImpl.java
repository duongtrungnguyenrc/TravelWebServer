package com.web.travel.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.web.travel.payload.response.AuthResponse;
import com.web.travel.service.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final FilesValidation filesValidation;
    private final Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    @Override
    public List<String> uploadMultiFile(MultipartFile[] files) throws IOException {
        EStatus eStatus = filesValidation.validate(files);
        switch (eStatus){
            case STATUS_EMPTY_FILE -> {
                throw new IOException("Lack of files");
            }
            case STATUS_WRONG_EXT -> {
                throw new IOException("Format is not supported");
            }
            default -> {
                List<String> fileNames = new ArrayList<>();
                for(MultipartFile file : files){
                    fileNames.add(uploadFile(file));
                }
                return fileNames;
            }
        }
    }
}
