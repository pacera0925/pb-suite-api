package com.paulcera.pb_suite_api.core.attendance.model;

import com.paulcera.pb_suite_api.security.model.WebUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private AttendancePhoto photo;

    public AttendanceRecord(WebUser webUser, AttendancePhoto photo) {
        this.webUser = webUser;
        this.tapTimestamp = Instant.now();
        this.photo = photo;
    }

}
