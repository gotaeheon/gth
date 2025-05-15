package org.fintech.portfolio.security;

import java.io.IOException;

import org.fintech.portfolio.dto.MemberSecurityDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// 카카오 로그인 API를 사용하여 로그인 성공 시 처리하는 핸들러 클래스
@RequiredArgsConstructor
@Log4j2
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    // 비밀번호 인코더 의존성 주입
    private final PasswordEncoder passwordEncoder;

    // 로그인 성공 후 호출되는 메소드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodePw = memberSecurityDTO.getMbPw();

        // 비밀번호가 없고 소셜 로그인 사용자일 경우
        if ((encodePw == null || encodePw.isEmpty()) && memberSecurityDTO.getMbSocial()) {
            response.sendRedirect("/member/memberModify");
            return;
        }

        // 기본 비밀번호 사용 중인 경우
        if ("1111".equals(encodePw) || passwordEncoder.matches("1111", encodePw)) {
            response.sendRedirect("/home");
            return;
        }

        // 정상 비밀번호 설정된 경우
        response.sendRedirect("/home");
    }
}
