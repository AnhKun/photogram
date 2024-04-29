package com.anguyen.photogram.service.impl;

import com.anguyen.photogram.converter.Converter;
import com.anguyen.photogram.dto.request.CommentRequest;
import com.anguyen.photogram.dto.response.CommentResponse;
import com.anguyen.photogram.dto.response.PageResponse;
import com.anguyen.photogram.entities.Comment;
import com.anguyen.photogram.entities.Post;
import com.anguyen.photogram.entities.UserEntity;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.repositories.CommentRepository;
import com.anguyen.photogram.repositories.PostRepository;
import com.anguyen.photogram.repositories.UserRepository;
import com.anguyen.photogram.security.services.UserDetailsImpl;
import com.anguyen.photogram.service.CommentService;
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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse addComment(String postId, CommentRequest commentRequest) {
        // get the authenticated user
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        UserEntity user = userRepository.findByIdAndStatusTrue(userDetails.getId()).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        // find the existing post by its id
        Post existingPost = postRepository.findByIdAndStatusTrue(postId).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found")
        );

        Comment newComment = Comment.builder()
                .content(commentRequest.getContent())
                .date(Instant.now())
                .post(existingPost)
                .status(true)
                .user(user)
                .build();

        Comment savedComment = commentRepository.save(newComment);

        CommentResponse response = CommentResponse.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .date(savedComment.getDate())
                .username(savedComment.getUser().getUsername())
                .build();

        return response;
    }

    @Override
    public PageResponse<CommentResponse> getAllCommentsByPost(String postId, int pageNo, int pageSize, String sortBy, String sortDir) {
        // get the post we want to see its comments
        Post existingPost = postRepository.findByIdAndStatusTrue(postId).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        // check sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Comment> commentPage = commentRepository.findAllByPostIdAndStatusTrue(postId, pageable);

        // get content from Page object
        List<Comment> commentList = commentPage.getContent();
        List<CommentResponse> content = Converter.toList(commentList, CommentResponse.class);

//        List<CommentResponse> responseList = new ArrayList<>();
//
//        commentList.forEach((comment) -> {
//            CommentResponse commentResponse = new CommentResponse();
//            CommentResponse.builder()
//                    .id(comment.getId())
//                    .content(comment.getContent())
//                    .date(comment.getDate())
//                    .username(comment.getUser().getUsername())
//                    .build();
//
//            responseList.add(commentResponse);
//        });

        PageResponse<CommentResponse> response = PageResponse.<CommentResponse>builder()
                .content(content)
                .pageNo(commentPage.getNumber())
                .pageSize(commentPage.getSize())
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .last(commentPage.isLast())
                .build();

        return response;
    }

    @Override
    public CommentResponse updateComment(String id, CommentRequest commentRequest) {
        // get the authenticated user
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        // find user's comment by id
        Comment existingComment = commentRepository.findByIdAndStatusTrue(id).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Comment not found")
        );

        if (!existingComment.getUser().getId().equals(userDetails.getId())) {
            throw new ApiException(ErrorCode.UPDATE_OTHERS);
        }

        // update comment
        existingComment.setDate(Instant.now());
        existingComment.setContent(commentRequest.getContent());

        // save the changes
        Comment updatedComment = commentRepository.save(existingComment);

        // return the data
        CommentResponse response = CommentResponse.builder()
                .id(updatedComment.getId())
                .content(updatedComment.getContent())
                .date(updatedComment.getDate())
                .username(updatedComment.getUser().getUsername())
                .build();

        return response;
    }

    @Override
    public String deleteComment(String id) {
        // get the authenticated user
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        // find the existing comment
        Comment existingComment = commentRepository.findByIdAndStatusTrue(id).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Comment not found"));

        if (!existingComment.getUser().getId().equals(userDetails.getId())) {
            throw new ApiException(ErrorCode.DELETE_OTHERS);
        }
        existingComment.setStatus(false);

        // delete comment
        commentRepository.save(existingComment);

        return "Comment deleted!";
    }

}
