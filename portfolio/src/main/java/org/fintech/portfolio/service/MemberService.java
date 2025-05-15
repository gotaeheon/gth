package org.fintech.portfolio.service;

import java.util.List;

import org.fintech.portfolio.dto.MemberDTO;
import org.fintech.portfolio.entity.MemberEntity;

public interface MemberService {
	
	// 회원 조회용
	MemberEntity findByMbId(String mbId); 
	
	// 로그인
    MemberEntity login(String mb_id, String mb_pw);
    
    // 회원가입
    MemberEntity join(MemberDTO memberDTO);
    
    // 중복체크
    boolean isIdDuplicate(String mbId);
    boolean isEmailDuplicate(String mbEmail);
    
    // 회원수정
    void modify(MemberDTO dto);
    
    // 회원탈퇴
    void withdraw(String mbId);
    
    // 전체회원 조회
    List<MemberEntity> findAll();

    // 탈퇴회원 복구
    void restore(String mbId);
    
    
   
}
