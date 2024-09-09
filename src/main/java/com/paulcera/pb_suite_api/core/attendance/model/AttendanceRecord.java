package com.paulcera.pb_suite_api.core.attendance.model;

import com.paulcera.pb_suite_api.security.model.WebUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance_record", indexes = {
    @Index(name = "idx_attendance_web_user_date", columnList = "web_user_id, tap_date")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "web_user_id", nullable = false)
    private WebUser webUser;

    @Column(nullable = false)
    private Instant tapTimestamp;

    @Column(name = "tap_date", nullable = false)
    private LocalDate tapDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private AttendancePhoto photo;

    @PrePersist
    protected void onCreate() {
        if (tapTimestamp == null) {
            tapTimestamp = Instant.now();
        }
        tapDate = tapTimestamp.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public AttendanceRecord(WebUser webUser, AttendancePhoto photo) {
        this.webUser = webUser;
        this.tapTimestamp = Instant.now();
        this.photo = photo;
    }

}
