package com.anguyen.photogram.service.impl;

import com.anguyen.photogram.dto.response.FileResponse;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.service.FileService;
import com.anguyen.photogram.util.FileValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final WebClient webClient;

    public FileServiceImpl() {
        this.webClient = WebClient.builder()
                .baseUrl("http://nocng.id.vn:8081/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46MTIzNDU2")
                .build();
    }

    @Override
    public Flux<FileResponse> uploadFiles(List<MultipartFile> files) throws IOException {

        files.stream().forEach(file -> {
            if (!FileValidator.isImage(file)) {
                throw new ApiException(ErrorCode.IMAGE_INVALID);
            }
        });

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            body.add("files", file.getResource());
        }

        return webClient.post()
                .uri("/minio/upload/files")
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToFlux(FileResponse.class);
    }

}
