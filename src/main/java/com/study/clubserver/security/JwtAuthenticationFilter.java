package com.study.clubserver.security;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER = "Bearer";

  private final Jwt jwt;

  public JwtAuthenticationFilter(Jwt jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String requestURI = httpServletRequest.getRequestURI();

    if (isUnauthenticatedUser()) {
      String token = resolveToken(httpServletRequest);
      if (token != null) {
        Claims claims = jwt.verifyToken(token).orElseThrow();

        if (canRefresh(claims, 300 * 1000L)) {  // 5분 전
          String refreshedToken = jwt.getRefreshedToken(token);
          response.setHeader(AUTHORIZATION_HEADER, String.format("%s %s", BEARER, refreshedToken));
          log.info("before token : {} / refreshed token : {}", token,refreshedToken);
        }

        Authentication authentication = jwt.getAuthentication(claims, token).orElseThrow();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("인증 정보를 저장했습니다. '{}' / URI : {}", authentication.getPrincipal(),requestURI);
      }
    }

    filterChain.doFilter(request, response);
  }

  private boolean isUnauthenticatedUser() {
    return SecurityContextHolder.getContext().getAuthentication() == null;
  }

  private String resolveToken(HttpServletRequest request) {
    String headerToken = request.getHeader(AUTHORIZATION_HEADER);
    String parameterToken = request.getParameter("token");

    if (StringUtils.hasText(headerToken) && headerToken.startsWith(BEARER)) {
      return headerToken.split(" ")[1];
    } else if (StringUtils.hasText(parameterToken) && parameterToken.startsWith(BEARER)) {
      return parameterToken.split(" ")[1];
    }
    return null;
  }

  private boolean canRefresh(Claims claims, long refreshRangeMills) {
    Date now = new Date();
    Date expiration = claims.getExpiration();

    if (expiration != null) {
      long remainTimeMills = expiration.getTime() - now.getTime();
      return remainTimeMills < refreshRangeMills;
    }
    return false;
  }

}
