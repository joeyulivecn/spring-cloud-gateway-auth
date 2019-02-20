package com.love.plus.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultErrorDto {
    private Integer code;
    private String message;
    private String details;

    public ResultErrorDto(ServiceErrorCode code, String details) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.details = details;
    }

    public ResultErrorDto(String message){
        this.message = message;
    }
}
