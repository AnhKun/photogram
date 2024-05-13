package com.anguyen.photogram.dto.request;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import lombok.*;

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
