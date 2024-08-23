package com.paulcera.pb_suite_api.security.model;

import com.paulcera.pb_suite_api.security.dto.FullNameDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class FullName {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    public FullName(FullNameDto fullName) {
        this.firstName = fullName.getFirstName();
        this.lastName = fullName.getLastName();
    }
}
