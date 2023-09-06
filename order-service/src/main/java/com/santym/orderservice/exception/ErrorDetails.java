package com.santym.orderservice.exception;

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
    private Object details;


}
