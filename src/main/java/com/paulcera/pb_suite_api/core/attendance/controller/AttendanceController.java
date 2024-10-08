package com.paulcera.pb_suite_api.core.attendance.controller;

import com.paulcera.pb_suite_api.core.attendance.dto.AttendanceLogForm;
import com.paulcera.pb_suite_api.core.attendance.dto.DailyAttendanceView;
import com.paulcera.pb_suite_api.core.attendance.service.AttendanceService;
import com.paulcera.pb_suite_api.core.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> logUserAttendance(@RequestParam Integer id,
        @RequestParam MultipartFile photo) {

        attendanceService.logUserAttendance(new AttendanceLogForm(id, photo));

        return ResponseEntity.ok(new ResponseMessage("Successfully logged user attendance."));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAttendance(@PageableDefault(size = 30) Pageable pageable) {

        Page<DailyAttendanceView> result = attendanceService.getAttendance(pageable);

        return ResponseEntity.ok(new ResponseMessage("Successfully retrieved attendance for all users", result));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseMessage> getAttendanceForUser(@PathVariable Integer userId,
        @PageableDefault(size = 30) Pageable pageable) {

        Page<DailyAttendanceView> result = attendanceService.getAttendanceForUser(userId, pageable);

        return ResponseEntity.ok(new ResponseMessage("Successfully retrieved attendance of user " + userId, result));
    }

}
