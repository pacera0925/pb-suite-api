package com.paulcera.pb_suite_api.core.attendance.dto;

import com.paulcera.pb_suite_api.core.attendance.model.AttendanceRecord;



public record UserAttendanceEntry(AttendanceRecord in, AttendanceRecord out) {

}
