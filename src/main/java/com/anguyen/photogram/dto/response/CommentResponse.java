package com.anguyen.photogram.dto.response;

import java.time.Instant;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private String id;
    private String content;
    private Instant date;
    private String username;
}
