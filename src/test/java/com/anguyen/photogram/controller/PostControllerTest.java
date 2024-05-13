package com.anguyen.photogram.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.anguyen.photogram.dto.request.PostRequest;
import com.anguyen.photogram.dto.response.PostResponse;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private PostRequest request;
    private PostResponse postResponse;

    @BeforeEach
    void initData() {
        request = PostRequest.builder()
                .caption("first post")
                .imageNames(List.of("test1.jpg", "test2.jpg"))
                .build();

        postResponse = PostResponse.builder()
                .id("6e8bc6184ddb")
                .caption("first post")
                .imageName(List.of("test1.jpg", "test2.jpg"))
                .date(Instant.now())
                .build();
    }

    @Test
    @WithMockUser
    void createPost_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(postService.addPost(ArgumentMatchers.any())).thenReturn(postResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("6e8bc6184ddb"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.caption").value("first post"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.imageName")
                        .value(Matchers.contains("test1.jpg", "test2.jpg")));
    }

    @Test
    @WithMockUser
    void getPostById_idMissing_fail() throws Exception {
        // GIVEN
        String postId = String.valueOf(UUID.randomUUID()); // Generate a random post ID
        Mockito.when(postService.getPost(postId))
                .thenThrow(new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Post not found"));
    }
}
