package com.anguyen.photogram.service;

import com.anguyen.photogram.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

public interface FileService {
    Flux<FileResponse> uploadFiles(List<MultipartFile> files) throws IOException;
}
