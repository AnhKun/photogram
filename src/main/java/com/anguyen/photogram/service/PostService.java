package com.anguyen.photogram.service;

import com.anguyen.photogram.dto.request.PostRequest;
import com.anguyen.photogram.dto.response.PageResponse;
import com.anguyen.photogram.dto.response.PostResponse;

public interface PostService {

    PostResponse addPost(PostRequest postRequest);

    PostResponse getPost(String id);

    PageResponse<PostResponse> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostResponse updatePost(String id, PostRequest postRequest);

    String deletePost(String id);
}
