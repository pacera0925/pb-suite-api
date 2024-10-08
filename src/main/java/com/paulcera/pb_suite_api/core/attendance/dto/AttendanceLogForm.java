package com.paulcera.pb_suite_api.core.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceLogForm {

    private Integer id;

    private MultipartFile photo;

}
