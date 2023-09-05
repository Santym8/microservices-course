package com.santym.inventoryservice.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class ErrorDetails {
    private Date timestamp;
    private String error;
    private String message;
    private String path;

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "timestamp=" + timestamp +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
