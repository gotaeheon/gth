package org.fintech.portfolio.dto;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.fintech.portfolio.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSecurityDTO extends User implements OAuth2User {

    private static final long serialVersionUID = 1L;

    private String mbId;
    private String mbPw;
    private String mbEmail;
    private Boolean mbDel; // Boolean 객체로 수정
    private Boolean mbSocial;

    private Map<String, Object> props;

    // 기존 생성자
    public MemberSecurityDTO(
            String username,
            String password,
            String email,
            Boolean del, // Boolean로 수정
            Boolean social,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mbId = username;
        this.mbPw = password;
        this.mbEmail = email;
        this.mbDel = del != null ? del : false; // null 처리
        this.mbSocial = social;
    }

    // MemberEntity를 받는 생성자 추가
    public MemberSecurityDTO(MemberEntity memberEntity) {
        super(
            memberEntity.getMbId(),
            memberEntity.getMbPw(),
            memberEntity.getRoleSet().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList())
        );

        this.mbId = memberEntity.getMbId();
        this.mbPw = memberEntity.getMbPw();
        this.mbEmail = memberEntity.getMbEmail();
        this.mbDel = memberEntity.getMbDeleted() != null ? memberEntity.getMbDeleted() : false; // null 체크 추가
        this.mbSocial = memberEntity.getMbSocial() != null ? memberEntity.getMbSocial() : false;
    }

    // OAuth2User 필수 구현 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return this.props;
    }

    @Override
    public String getName() {
        return this.mbId;
    }
}
