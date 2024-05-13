package com.anguyen.photogram.service.impl;

import com.anguyen.photogram.converter.Converter;
import com.anguyen.photogram.dto.request.PostRequest;
import com.anguyen.photogram.dto.response.PageResponse;
import com.anguyen.photogram.dto.response.PostResponse;
import com.anguyen.photogram.entities.Post;
import com.anguyen.photogram.entities.UserEntity;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.repositories.PostRepository;
import com.anguyen.photogram.repositories.UserRepository;
import com.anguyen.photogram.security.services.UserDetailsImpl;
import com.anguyen.photogram.service.PostService;
import com.anguyen.photogram.util.JwtSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponse addPost(PostRequest postRequest) {
        // get the authenticated user
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found1"));

        UserEntity user = userRepository.findByIdAndStatusTrue(userDetails.getId()).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        // create a post
        Post newPost = Post.builder()
                .caption(postRequest.getCaption())
                .imageName(postRequest.getImageNames())
                .date(Instant.now())
                .status(true)
                .user(user)
                .build();

        // 4. save the post object
        Post post = postRepository.save(newPost);

        return Converter.toModel(post, PostResponse.class);
    }

    @Override
    public PostResponse getPost(String id) {
        // check the data in DB and if exists, fetch the data of given id
        Post post = postRepository.findByIdAndStatusTrue(id).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        // return the data
        return Converter.toModel(post, PostResponse.class);
    }

    @Override
    public PageResponse<PostResponse> getAllPosts(String userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        // get the user
        UserDetailsImpl user = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        if (userId == null) {
            userId = user.getId();
        }

        // check sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> postPage = postRepository.findAllByUserIdAndStatusTrue(userId, pageable);

        // get content from Page object
        List<Post> postList = postPage.getContent();
        List<PostResponse> content = Converter.toList(postList, PostResponse.class);

        return PageResponse.<PostResponse>builder()
                .content(content)
                .pageNo(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .last(postPage.isLast())
                .build();
    }

    @Override
    public PostResponse updatePost(String id, PostRequest postRequest) {
        // get the authenticated user
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));


        Post post = postRepository.findByIdAndStatusTrue(id).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        if (!post.getUser().getId().equals(userDetails.getId())) {
            throw new ApiException(ErrorCode.UPDATE_OTHERS);
        } else {
            post.setCaption(postRequest.getCaption());
            post.setDate(Instant.now());
        }

        Post updatedPost = postRepository.save(post);

        return Converter.toModel(updatedPost, PostResponse.class);
    }

    @Override
    public String deletePost(String id) {
        // get the authenticated user
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        Post post = postRepository.findByIdAndStatusTrue(id).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        if (!post.getUser().getId().equals(userDetails.getId())) {
            throw new ApiException(ErrorCode.DELETE_OTHERS);
        }

        post.setStatus(false);

        postRepository.save(post);
        return "Deleted!";
    }

}
