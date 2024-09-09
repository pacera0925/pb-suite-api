package com.paulcera.pb_suite_api.core.attendance.repository;

import com.paulcera.pb_suite_api.core.attendance.dto.DailyAttendanceView;
import com.paulcera.pb_suite_api.core.attendance.model.AttendanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {

    @Query("""
            SELECT new com.paulcera.pb_suite_api.core.attendance.dto.DailyAttendanceView(
                u.id, u.fullName.firstName, u.fullName.lastName,
                MIN(a.tapTimestamp),
                MAX(a.tapTimestamp)
            )
            FROM AttendanceRecord a
            INNER JOIN a.webUser u
            GROUP BY a.tapDate, u.id, u.fullName.firstName, u.fullName.lastName
            ORDER BY MIN(a.tapTimestamp) DESC
        """)
    Page<DailyAttendanceView> findAllPageable(Pageable pageable);

    @Query("""
            SELECT new com.paulcera.pb_suite_api.core.attendance.dto.DailyAttendanceView(
                u.id, u.fullName.firstName, u.fullName.lastName,
                MIN(a.tapTimestamp),
                MAX(a.tapTimestamp)
            )
            FROM AttendanceRecord a
            INNER JOIN a.webUser u
            WHERE u.id = :userId
            GROUP BY a.tapDate, u.id, u.fullName.firstName, u.fullName.lastName
            ORDER BY MIN(a.tapTimestamp) DESC
        """)
    Page<DailyAttendanceView> findByUserIdPageable(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT ar FROM AttendanceRecord ar ORDER BY ar.tapTimestamp DESC LIMIT 1")
    AttendanceRecord findLatestCreated();
}
