package com.anguyen.photogram.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String id;
    private String caption;
    private List<String> imageName;
    private Instant date;
}
