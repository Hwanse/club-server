package com.study.clubserver.config.web.argument;

import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountRepository;
import com.study.clubserver.security.CurrentAccount;
import com.study.clubserver.security.JwtAuthentication;
import com.study.clubserver.security.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

  private final AccountRepository accountRepository;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.hasParameterAnnotation(CurrentAccount.class);
  }

  @Override
  public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
    NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
    JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    JwtAuthentication authentication = (JwtAuthentication) token.getPrincipal();

    Account account = accountRepository.findWithAccountRoleByUserId(authentication.getUserId())
                                       .orElseThrow(
                                         () -> new UsernameNotFoundException(authentication.getUserId() + " 해당 유저를 찾을 수 없습니다.")
                                       );
    return account;
  }
}
