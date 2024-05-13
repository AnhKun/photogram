package com.anguyen.photogram.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anguyen.photogram.dto.response.FileResponse;
import com.anguyen.photogram.service.FileService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Flux<FileResponse>> uploadFile(@RequestPart(name = "file") List<MultipartFile> files)
            throws IOException {
        var response = fileService.uploadFiles(files);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
