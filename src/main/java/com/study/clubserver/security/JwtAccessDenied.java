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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAccessDenied implements AccessDeniedHandler {

  private final ApiResult<?> errorResponse = ERROR(HttpStatus.FORBIDDEN, "Authentication error (cause: forbidden)");

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
    AccessDeniedException e) throws IOException, ServletException {

    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    httpServletResponse.getWriter().flush();
    httpServletResponse.getWriter().close();
  }
}
