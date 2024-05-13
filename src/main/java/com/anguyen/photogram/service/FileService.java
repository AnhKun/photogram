package com.anguyen.photogram.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.anguyen.photogram.dto.response.FileResponse;

import reactor.core.publisher.Flux;

public interface FileService {
    Flux<FileResponse> uploadFiles(List<MultipartFile> files) throws IOException;
}
