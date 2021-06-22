package com.study.clubserver.config.web;

import com.study.clubserver.config.web.argument.AuthArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@RequiredArgsConstructor
public class WebConfig extends WebMvcConfigurationSupport {

  private final AuthArgumentResolver authArgumentResolver;

  @Override
  protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(authArgumentResolver);
  }

}
