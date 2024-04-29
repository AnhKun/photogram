package com.anguyen.photogram.service;

import com.anguyen.photogram.dto.request.CommentRequest;
import com.anguyen.photogram.dto.response.CommentResponse;
import com.anguyen.photogram.dto.response.PageResponse;

public interface CommentService {

    CommentResponse addComment(String postId, CommentRequest commentRequest);

    PageResponse<CommentResponse> getAllCommentsByPost(String postId, int pageNo, int pageSize, String sortBy, String sortDir);

    CommentResponse updateComment(String id, CommentRequest commentRequest);

    String deleteComment(String id);
}
