package com.study.clubserver.api.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResult<T> {

  private T data;

  private boolean success;

  private ApiError error;

  public ApiResult(T data, boolean success, ApiError error) {
    this.data = data;
    this.success = success;
    this.error = error;
  }

  public static <T> ApiResult<T> OK(T data) {
    return new ApiResult<>(data, true, null);
  }

  public static ApiResult<?> ERROR(HttpStatus status, Throwable throwable) {
    return new ApiResult<>(null, false, new ApiError(status, throwable));
  }

  public static ApiResult<?> ERROR(HttpStatus status, String message) {
    return new ApiResult<>(null, false, new ApiError(status, message));
  }

}
