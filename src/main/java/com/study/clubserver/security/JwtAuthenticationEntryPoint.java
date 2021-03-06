package com.study.clubserver.security;


import static com.study.clubserver.api.dto.ApiResult.ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.clubserver.api.dto.ApiResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ApiResult<?> errorResponse = ERROR(HttpStatus.UNAUTHORIZED, "Authentication error (cause: unauthorized)");

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse, AuthenticationException e)
    throws IOException, ServletException {

    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    httpServletResponse.getWriter().flush();
    httpServletResponse.getWriter().close();
  }
}
