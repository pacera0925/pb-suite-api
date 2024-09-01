package com.paulcera.pb_suite_api.core.attendance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.paulcera.pb_suite_api.core.attendance.dto.AttendanceLogForm;
import com.paulcera.pb_suite_api.core.attendance.dto.AttendanceLogFormMother;
import com.paulcera.pb_suite_api.core.attendance.model.AttendanceRecord;
import com.paulcera.pb_suite_api.core.attendance.repository.AttendanceRecordRepository;
import com.paulcera.pb_suite_api.core.exception.UserNotFoundException;
import com.paulcera.pb_suite_api.security.model.WebUserMother;
import com.paulcera.pb_suite_api.security.repository.WebUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @InjectMocks
    private AttendanceService attendanceService;

    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;

    @Mock
    private WebUserRepository webUserRepository;

    @Test
    void logUserAttendance_nonExistingUser_throwsException() {
        AttendanceLogForm attendanceLogForm = AttendanceLogFormMother.valid();
        when(webUserRepository.findById(attendanceLogForm.getId())).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class,
            () -> attendanceService.logUserAttendance(attendanceLogForm),
            "Expected logUserAttendance() to throw UserNotFoundException, but it didn't");

        assertEquals("No user found with id: 1", thrown.getMessage());
    }

    @Test
    void logUserAttendance_existingUser_success() {
        AttendanceLogForm attendanceLogForm = AttendanceLogFormMother.valid();
        when(webUserRepository.findById(attendanceLogForm.getId())).thenReturn(Optional.of(WebUserMother.admin()));

        attendanceService.logUserAttendance(attendanceLogForm);

        verify(attendanceRecordRepository, times(1)).save(any(AttendanceRecord.class));
    }

}