package com.study.clubserver.api.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiError {

  private int statusCode;

  private String message;

  public ApiError(HttpStatus status, String message) {
    this.statusCode = status.value();
    this.message = message;
  }

  public ApiError(HttpStatus status, Throwable throwable) {
    this(status, throwable.getMessage());
  }

}
