package org.fintech.portfolio.security;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.fintech.portfolio.dto.MemberSecurityDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.MemberRole;
import org.fintech.portfolio.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("OAuth2UserRequest: {}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("OAuth2 Provider: {}", clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        if ("kakao".equals(clientName)) {
            email = getKakaoEmail(paramMap);
        }

        return generateDTO(email, paramMap);  
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        Object accountObj = paramMap.get("kakao_account");
        if (accountObj instanceof Map) {
            Map<String, Object> accountMap = (Map<String, Object>) accountObj;
            return (String) accountMap.get("email");
        }
        return null;
    }

    private MemberSecurityDTO generateDTO(String mbEmail, Map<String, Object> params) {
        Optional<MemberEntity> result = memberRepository.findByMbEmail(mbEmail);

        if (result.isEmpty()) {
            // 회원이 없을 경우 새로 생성
            MemberEntity member = MemberEntity.builder()
                .mbId(mbEmail)
                .mbPw(passwordEncoder.encode("1111"))
                .mbEmail(mbEmail)
                .mbSocial(true)
                .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMbId(),
                member.getMbPw(),
                member.getMbEmail(),
                false,
                true,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
            );

            memberSecurityDTO.setProps(params);
            return memberSecurityDTO;

        } else {
            // 이미 회원이 있을 경우
            MemberEntity member = result.get();

            // MemberEntity를 MemberSecurityDTO로 변환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member);
            memberSecurityDTO.setProps(params);
            return memberSecurityDTO;
        }
    }
}
