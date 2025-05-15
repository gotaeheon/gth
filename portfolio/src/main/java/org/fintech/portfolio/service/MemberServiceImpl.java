package org.fintech.portfolio.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.fintech.portfolio.dto.MemberDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.MemberRole;
import org.fintech.portfolio.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 회원조회
    @Override
    public MemberEntity findByMbId(String mbId) {
        return memberRepository.findByMbId(mbId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // 로그인
    @Override
    public MemberEntity login(String mbId, String mbPw) {
        MemberEntity member = memberRepository.findByMbIdAndMbPw(mbId, mbPw);

        if (member == null) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (Boolean.TRUE.equals(member.getMbDeleted())) {
            throw new IllegalStateException("탈퇴한 계정입니다. 관리자에게 문의하세요.");
        }

        return member;
    }

    // 회원가입
    @Override
    public MemberEntity join(MemberDTO memberDTO) {
        // 중복 체크
        if (memberRepository.existsByMbId(memberDTO.getMbId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        if (memberRepository.existsByMbEmail(memberDTO.getMbEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberDTO.getMbPw());

        // DTO -> Entity 변환
        MemberEntity memberEntity = MemberEntity.toEntity(memberDTO);
        
        // 역할 설정
        if (memberEntity.getRoleSet() == null) {
            memberEntity.setRoleSet(new HashSet<>());
        }
        memberEntity.getRoleSet().add(MemberRole.USER); // 기본 권한 USER 부여

        // 암호화된 비밀번호 설정
        memberEntity.setMbPw(encodedPassword);

        return memberRepository.save(memberEntity); // DB에 저장
    }

    // 아이디 중복체크
    @Override
    public boolean isIdDuplicate(String mbId) {
        return memberRepository.existsByMbId(mbId);
    }

    // 이메일 중복체크
    @Override
    public boolean isEmailDuplicate(String mbEmail) {
        return memberRepository.existsByMbEmail(mbEmail);
    }

    // 회원수정
    @Override
    public void modify(MemberDTO dto) {
        MemberEntity entity = memberRepository.findById(dto.getMbId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        entity.setMbName(dto.getMbName());
        entity.setMbNick(dto.getMbNick());
        entity.setMbGender(dto.getMbGender());
        entity.setMbHp(dto.getMbHp());
        entity.setMbEmail(dto.getMbEmail());

        memberRepository.save(entity);
    }

    // 회원탈퇴
    @Override
    public void withdraw(String mbId) {
        Optional<MemberEntity> optionalMember = memberRepository.findById(mbId);

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            member.setMbDeleted(true); // 탈퇴 처리
            memberRepository.save(member); // 저장
        }
    }

    // 모든회원조회
    @Override
    public List<MemberEntity> findAll() {
        return memberRepository.findAll();
    }

    // 회원복구
    @Override
    public void restore(String mbId) {
        MemberEntity member = memberRepository.findByMbId(mbId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        member.setMbDeleted(false); // 복구
        memberRepository.save(member);
    }
}
