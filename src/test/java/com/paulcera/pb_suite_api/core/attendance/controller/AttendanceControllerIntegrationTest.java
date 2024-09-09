package com.paulcera.pb_suite_api.core.attendance.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paulcera.pb_suite_api.core.attendance.model.AttendanceRecord;
import com.paulcera.pb_suite_api.core.attendance.repository.AttendanceRecordRepository;
import com.paulcera.pb_suite_api.core.attendance.service.AttendanceService;
import com.paulcera.pb_suite_api.core.controller.BaseIntegrationTestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@Sql(scripts = "/attendance-controller-dataset.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AttendanceControllerIntegrationTest extends BaseIntegrationTestController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void logUserAttendance_validRequest_success() throws Exception {
        int userId = 1;
        MockMultipartFile photo = new MockMultipartFile(
            "photo", "test-photo.jpg", "image/jpeg", "some-image-data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/attendance")
                .file(photo)
                .param("id", String.valueOf(userId))
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Successfully logged user attendance."));

        AttendanceRecord createdRecord = attendanceRecordRepository.findLatestCreated();
        assertEquals(userId, createdRecord.getWebUser().getId());
        assertNotNull(createdRecord.getPhoto());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void logUserAttendance_invalidNotExistingUser_badRequest() throws Exception {
        int userId = 2;
        MockMultipartFile photo = new MockMultipartFile(
            "photo", "test-photo.jpg", "image/jpeg", "some-image-data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/attendance")
                .file(photo)
                .param("id", String.valueOf(userId))
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("No user found with id: " + userId));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void getAttendance_retrieveAttendanceList() throws Exception {
        mockMvc.perform(get("/api/attendance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Successfully retrieved attendance for all users"))
            .andExpect(jsonPath("$.payload").isNotEmpty())
            .andExpect(jsonPath("$.payload.content").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void getAttendanceForUser_invalidNonExistingUser_badRequest() throws Exception {
        mockMvc.perform(get("/api/attendance/user/0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("No user found with id: 0"))
            .andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void getAttendanceForUser_validExistingUser_success() throws Exception {
        mockMvc.perform(get("/api/attendance/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Successfully retrieved attendance of user 1"))
            .andExpect(jsonPath("$.payload").isNotEmpty())
            .andExpect(jsonPath("$.payload.content").isNotEmpty());
    }
}