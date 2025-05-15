package org.fintech.portfolio.config;

import javax.sql.DataSource;

import org.fintech.portfolio.security.CustomSocialLoginSuccessHandler;
import org.fintech.portfolio.security.CustomUserDetailsService;
import org.fintech.portfolio.security.Custom403Handler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@RequiredArgsConstructor
// 보안에 관련된 어노테이션 활성화
@EnableMethodSecurity
@Log4j2
public class CustomSecurityConfig {
	
	//p704
		//데이터베이스 연결 기능
		private final DataSource dataSource;
		//로그인한 사용자 정보
		private final CustomUserDetailsService userDetailsService;
	
	
	// 비밀번호 암호화 방식 설정 (BCryptPasswordEncoder 사용)
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	// SecurityFilterChain 설정 - HTTP 요청에 대한 보안 설정
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			.formLogin(login -> login.loginPage("/member/login")
			     //정상적으로 로그인시 이동하는 URL 선언
				.usernameParameter("mbId")
				.passwordParameter("mbPw")
				 .defaultSuccessUrl("/home", true))
	        .logout(logout -> logout
	            .logoutUrl("/member/logout")  
	            .logoutSuccessUrl("/member/login") 
	        )
	        .rememberMe(rememberMe -> rememberMe.key("12345678")
					.tokenRepository(persistentTokenRepository())
					.userDetailsService(userDetailsService)
					.tokenValiditySeconds(60 * 60 * 24 * 30))//유효기간 30일 
	        
	        .csrf(csrf -> csrf.disable()
	        		
	        )
	        .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler()))
	        .oauth2Login(oauth2Login -> oauth2Login.loginPage("/member/login").successHandler(authenticationSuccessHandler()));

	    return http.build();
	}


	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		
		//자동로그인 토큰관련 정보를 persistent_logins 테이블에 반영
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		
		repo.setDataSource(dataSource);
		
		return repo;
	}

	// 정적 리소스 파일들 (CSS, JS, 이미지 등)에 대해 보안 적용을 제외하는 설정
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 정적 리소스 파일들이 위치한 경로에 대해 보안 적용을 배제
		// PathRequest.toStaticResources().atCommonLocations()는 static 폴더 내의 모든 자원에 대해 보안 검사를 하지 않도록 설정
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		
		return new CustomSocialLoginSuccessHandler(passwordEncoder());
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		
		return new Custom403Handler();
	}
}
