package com.paulcera.pb_suite_api.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.paulcera.pb_suite_api.core.exception.InvalidFileException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class FileUtilTest {

    @Test
    void getFileBytes_invalidFile_throwsException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("File error"));

        InvalidFileException thrown = assertThrows(InvalidFileException.class, () -> FileUtil.getFileBytes(mockFile),
            "Expected getFileBytes() to throw InvalidFileException, but it didn't");

        assertEquals("Could not extract file content.", thrown.getMessage());
    }

    @Test
    void getFileBytes_validFile_success() {
        MultipartFile mockFile = new MockMultipartFile(
            "photo",
            "test-photo.jpg",
            "image/jpeg",
            "some-image-data".getBytes()
        );

        byte[] result = FileUtil.getFileBytes(mockFile);
        assertNotNull(result);
    }
}