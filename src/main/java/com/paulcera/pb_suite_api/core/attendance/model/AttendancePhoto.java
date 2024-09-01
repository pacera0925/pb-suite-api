package com.paulcera.pb_suite_api.core.attendance.model;

import com.paulcera.pb_suite_api.core.util.FileUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendancePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(nullable = false)
    private byte[] photoData;

    @OneToOne(mappedBy = "photo")
    private AttendanceRecord attendanceRecord;

    public static AttendancePhoto createFromMultipartFile(MultipartFile photo) {
        AttendancePhoto attendancePhoto = new AttendancePhoto();
        attendancePhoto.setPhotoData(FileUtil.getFileBytes(photo));
        return attendancePhoto;
    }
}
