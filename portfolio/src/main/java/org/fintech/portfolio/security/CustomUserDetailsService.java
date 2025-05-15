package org.fintech.portfolio.security;

import org.fintech.portfolio.dto.MemberSecurityDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String mbId) throws UsernameNotFoundException {
    	
        log.info("로그인 시도한 사용자 ID: " + mbId);
        
        MemberEntity member = memberRepository.findById(mbId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + mbId));

        return new MemberSecurityDTO(member);
    }
}





