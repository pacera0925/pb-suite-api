package com.paulcera.pb_suite_api.security.dto;

import com.paulcera.pb_suite_api.security.model.WebUser;
import lombok.Data;

@Data
public class WebUserView {

    private Integer id;

    private String username;

    private FullNameDto fullName;


    public WebUserView(WebUser newWebUser) {
        this.id = newWebUser.getId();
        this.username = newWebUser.getUsername();
        this.fullName = new FullNameDto(newWebUser.getFullName());
    }
}
