package com.anguyen.photogram.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    @Length(max = 200, message = "The caption is not over 200 letters")
    private String caption;
    private List<String> imageNames;
}
