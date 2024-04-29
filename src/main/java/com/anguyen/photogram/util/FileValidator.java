package com.anguyen.photogram.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
    public static boolean isImage(MultipartFile file) {
        return file.getContentType() != null &&
                (file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) ||
                        file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) ||
                        file.getContentType().equals(MediaType.IMAGE_GIF_VALUE));
    }

    private FileValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
