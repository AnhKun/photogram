package com.anguyen.photogram.service;

import com.anguyen.photogram.dto.request.PostRequest;
import com.anguyen.photogram.dto.response.PostResponse;
import com.anguyen.photogram.entities.Post;
import com.anguyen.photogram.entities.UserEntity;
import com.anguyen.photogram.repositories.PostRepository;
import com.anguyen.photogram.repositories.UserRepository;
import com.anguyen.photogram.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class PostServiceTest {
    @Autowired
    private PostServiceImpl postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    private PostRequest request;
    private PostResponse postResponse;
    private Post post;
    private UserEntity user;


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

        user = UserEntity.builder()
                .id("userId")
                .email("fake@gmail.com")
                .username("username")
                .password("password")
                .build();

        post = Post.builder()
                .id("6e8bc6184ddb")
                .caption("first post")
                .imageName(List.of("test1.jpg", "test2.jpg"))
                .date(Instant.now())
                .user(user)
                .build();

    }


    @Test
    @WithMockUser(username = "username")
    void getPost_validRequest_success() {
        //GIVEN
        when(postRepository.findByIdAndStatusTrue(any())).thenReturn(Optional.ofNullable(post));

        // WHEN
        var response = postService.getPost(any());
        // THEN

        assertThat(response.getId()).isEqualTo("6e8bc6184ddb");
        assertThat(response.getCaption()).isEqualTo("first post");
        assertThat(response.getImageName()).contains("test1.jpg", "test2.jpg");
    }
}
