package com.venzee.register_app.dto.response.exception;

import lombok.Data;

import java.util.Date;

@Data
public class CommonExceptionResponse {
    private int status;
    private String message;
    private Date timeStamp;
}
