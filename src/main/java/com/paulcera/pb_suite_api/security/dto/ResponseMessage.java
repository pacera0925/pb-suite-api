package com.paulcera.pb_suite_api.security.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseMessage {

    private String message;

    private Object content;

    public ResponseMessage(String message) {
        this(message, null);
    }

    public ResponseMessage(String message, Object content) {
        this.message = message;
        this.content = content;
    }
}
