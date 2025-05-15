package org.fintech.portfolio;

import java.util.Optional;
import java.util.stream.IntStream;

import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.MemberRole;
import org.fintech.portfolio.repository.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

	@Autowired //자동주입
	private MemberRepository memberRepository;
	
	//비밀번호 암호화 처리
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	//100명 회원등록및 ROLE 저장
	public void insertMembers() {
		
		IntStream.rangeClosed(1, 100)
				 .forEach(i -> {
					 //Member Entity 객체에 필드에 값을 대입
					 MemberEntity member = MemberEntity.builder()
							 			 .mbId("member" + i)
							 			 .mbPw(passwordEncoder.encode("1111"))
							 			 .mbEmail("email" + i + "@naver.com")
							 			 .build();
		
		//사용자의 Role 지정			 
		member.addRole(MemberRole.USER);
		
		//i값이 90이상이면 관리자 Role 부여
		if(i >= 90) {
			member.addRole(MemberRole.ADMIN);
		}
		
		//tbl_member테이블에 저장
		memberRepository.save(member);
		
		});
	}
	
	//p725 특정 회원의 권한 Role 조회
	@Disabled
	public void testRead() {
		
		Optional<MemberEntity> result = memberRepository.getWithRoles("member100");
		
		MemberEntity member = result.orElseThrow();
		
		//권한을 조회하여 반복문으로 출력
		member.getRoleSet()
			  .forEach(memberRole -> log.info(memberRole.name()));
	}
	
	
}//



