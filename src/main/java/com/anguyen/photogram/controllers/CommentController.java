package com.anguyen.photogram.controllers;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anguyen.photogram.dto.request.CommentRequest;
import com.anguyen.photogram.dto.response.ApiResponse;
import com.anguyen.photogram.dto.response.CommentResponse;
import com.anguyen.photogram.dto.response.PageResponse;
import com.anguyen.photogram.service.CommentService;
import com.anguyen.photogram.util.AppConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable String postId, @RequestBody @Valid CommentRequest commentRequest) {
        CommentResponse newComment = commentService.addComment(postId, commentRequest);

        ApiResponse<CommentResponse> response = ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED)
                .result(newComment)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PageResponse<CommentResponse>> getAllCommentsByPost(
            @PathVariable String postId,
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)
                    int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "date", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
                    String sortDir) {

        PageResponse<CommentResponse> response =
                commentService.getAllCommentsByPost(postId, pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable String id, @RequestBody @Valid CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateComment(id, commentRequest);

        ApiResponse<CommentResponse> response = ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK)
                .result(updatedComment)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable String id) {
        String statement = commentService.deleteComment(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(HttpStatus.OK)
                .result(statement)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
