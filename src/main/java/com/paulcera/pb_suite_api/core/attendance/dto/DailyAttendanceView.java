package com.paulcera.pb_suite_api.core.attendance.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyAttendanceView {

    private Integer userId;

    private String firstName;

    private String lastName;

    private Instant in;

    private Instant out;

}
