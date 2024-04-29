package com.anguyen.photogram.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private String filename;
    private String contentType;
    private int fileSize;
    private String createdTime;
}
