package com.anguyen.photogram.controllers;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anguyen.photogram.dto.request.PostRequest;
import com.anguyen.photogram.dto.response.ApiResponse;
import com.anguyen.photogram.dto.response.PageResponse;
import com.anguyen.photogram.dto.response.PostResponse;
import com.anguyen.photogram.service.PostService;
import com.anguyen.photogram.util.AppConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> addPostHandler(@RequestBody PostRequest postRequest) {

        PostResponse response = postService.addPost(postRequest);

        ApiResponse<PostResponse> body = ApiResponse.<PostResponse>builder()
                .code(HttpStatus.CREATED)
                .result(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable String id) {
        ApiResponse<PostResponse> body = ApiResponse.<PostResponse>builder()
                .code(HttpStatus.OK)
                .result(postService.getPost(id))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/posts")
    public ResponseEntity<PageResponse<PostResponse>> getAllPost(
            @RequestParam(name = "userId", defaultValue = "", required = false) String userId,
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)
                    int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "date", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "dsc", required = false) String sortDir) {
        PageResponse<PostResponse> response = postService.getAllPosts(userId, pageNo, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable String id, @RequestBody @Valid PostRequest postRequest) {

        ApiResponse<PostResponse> body = ApiResponse.<PostResponse>builder()
                .code(HttpStatus.OK)
                .result(postService.updatePost(id, postRequest))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable String id) {
        String statement = postService.deletePost(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(HttpStatus.OK)
                .result(statement)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
