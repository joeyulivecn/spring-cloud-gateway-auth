package com.love.plus.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
Result:

{
  "success": true,
  "data": {
    "personId": 42
  },
  "error": null,
}

Result with errors:

{
  "success": false,
  "data": null,
  "error": {
    "message": "An internal error occurred during your request!",
    "details": "..."
  },
}
 */

@Getter
@Setter
@NoArgsConstructor
public class ResultDto<T> {
    private boolean success;
    private T data;
    private ResultErrorDto error;

    public static <T> ResultDto<T> success(T data) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(true);
        resultDto.setData(data);
        return resultDto;
    }

    public static <T> ResultDto<T> success() {
        return success(null);
    }

    public static <T> ResultDto<T> error(ResultErrorDto error) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        resultDto.setError(error);
        return resultDto;
    }

    public static <T> ResultDto<T> error(ServiceErrorCode code) {
        return error(code, null);
    }

    public static <T> ResultDto<T> error(String message) {
        return error(message);
    }

    public static <T> ResultDto<T> error(ServiceErrorCode code, String details) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        resultDto.setError(new ResultErrorDto(code, details));
        return resultDto;
    }
}
