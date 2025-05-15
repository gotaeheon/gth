package org.fintech.portfolio.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		//권한이 없는 사용자가 URL에 접속시도시
		//HttpStatus.FORBIDDEN.value() => 403
		response.setStatus(HttpStatus.FORBIDDEN.value());
		
		String contentType = request.getHeader("Content-Type");
		
		//클라이언트 요청이 JSON형태인지 체크
		boolean jsonRequest = contentType.startsWith("application/json");
		
		log.info(jsonRequest);
		
		//클라이언트 요청 데이터의 MIME이 JSON 형태가 
		//아니면 다시 로그인 화면으로 이동
		if(!jsonRequest) {
			response.sendRedirect("/member/login?error=ACCESS_DENIED");
		}
	}

}




