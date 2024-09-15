package com.venzee.register_app.dto.response;

import lombok.Data;

import java.util.Date;
@Data
public class CommonResponse {
    private int status;
    private String message;
    private Date timeStamp;
}
