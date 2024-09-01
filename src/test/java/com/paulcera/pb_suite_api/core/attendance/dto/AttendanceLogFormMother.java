package com.paulcera.pb_suite_api.core.attendance.dto;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class AttendanceLogFormMother {

    public static AttendanceLogForm valid() {
        AttendanceLogForm form = new AttendanceLogForm();
        form.setId(1);
        MultipartFile mockFile = new MockMultipartFile(
            "photo",
            "test-photo.jpg",
            "image/jpeg",
            "some-image-data".getBytes()
        );
        form.setPhoto(mockFile);
        return form;
    }

}
