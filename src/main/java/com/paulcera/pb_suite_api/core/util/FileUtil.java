package com.paulcera.pb_suite_api.core.util;

import com.paulcera.pb_suite_api.core.exception.InvalidFileException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static byte[] getFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new InvalidFileException("Could not extract file content.");
        }
    }

}
