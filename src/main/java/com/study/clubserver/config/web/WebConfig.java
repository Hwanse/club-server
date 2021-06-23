package com.study.clubserver.config.web;

import com.study.clubserver.config.web.argument.AuthArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@RequiredArgsConstructor
public class WebConfig extends WebMvcConfigurationSupport {

  private final AuthArgumentResolver authArgumentResolver;

  @Override
  protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(authArgumentResolver);
    /**
     *  PageableHandlerMethodArgumentResolver는 컨트롤러 메소드에 Pageable 타입의 파라미터가 존재하는 경우,
     *  요청 파라미터를 토대로 PageRequest 객체를 생성한다. 컨트롤러의 메소드 파라미터로 Pageable 타입이
     *  존재하는 경우, Spring MVC가 요청 파라미터로부터 Pageable 객체를 생성하려고 시도한다
     */
    argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
  }

}
