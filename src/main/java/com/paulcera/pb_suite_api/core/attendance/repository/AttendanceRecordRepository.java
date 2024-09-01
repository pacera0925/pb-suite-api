package com.paulcera.pb_suite_api.core.attendance.repository;

import com.paulcera.pb_suite_api.core.attendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {

}
