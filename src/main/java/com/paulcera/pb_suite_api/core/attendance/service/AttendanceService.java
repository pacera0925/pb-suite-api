package com.paulcera.pb_suite_api.core.attendance.service;

import com.paulcera.pb_suite_api.core.attendance.dto.AttendanceLogForm;
import com.paulcera.pb_suite_api.core.attendance.dto.DailyAttendanceView;
import com.paulcera.pb_suite_api.core.attendance.model.AttendancePhoto;
import com.paulcera.pb_suite_api.core.attendance.model.AttendanceRecord;
import com.paulcera.pb_suite_api.core.attendance.repository.AttendanceRecordRepository;
import com.paulcera.pb_suite_api.core.exception.UserNotFoundException;
import com.paulcera.pb_suite_api.security.model.WebUser;
import com.paulcera.pb_suite_api.security.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final WebUserRepository webUserRepository;

    @Autowired
    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository,
        WebUserRepository webUserRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.webUserRepository = webUserRepository;
    }

    @PreAuthorize("hasAuthority('USER')")
    public void logUserAttendance(AttendanceLogForm form) {
        WebUser webUser = webUserRepository.findById(form.getId())
            .orElseThrow(() -> new UserNotFoundException("No user found with id: " + form.getId()));

        AttendancePhoto photo = AttendancePhoto.createFromMultipartFile(form.getPhoto());

        attendanceRecordRepository.save(new AttendanceRecord(webUser, photo));
    }

    @PreAuthorize("hasAuthority('USER')")
    public Page<DailyAttendanceView> getAttendance(Pageable pageable) {
        return attendanceRecordRepository.findAllPageable(pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    public Page<DailyAttendanceView> getAttendanceForUser(Integer userId, Pageable pageable) {
        boolean isUserExisting = webUserRepository.existsById(userId);
        if (!isUserExisting) {
            throw new UserNotFoundException("No user found with id: " + userId);
        }

        return attendanceRecordRepository.findByUserIdPageable(userId, pageable);
    }
}
