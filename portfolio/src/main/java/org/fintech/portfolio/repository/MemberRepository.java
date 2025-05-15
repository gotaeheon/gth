package org.fintech.portfolio.repository;

import java.util.Optional;

import org.fintech.portfolio.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    // 로그인 처리
    MemberEntity findByMbIdAndMbPw(String mbId, String mbPw);
    
    //특정 회원의 Role 정보를 가져오는 추상메서드
    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT m FROM MemberEntity m WHERE m.mbId = :mbId")
    Optional<MemberEntity> getWithRoles(@Param("mbId") String mbId);
    
    //p753
	@EntityGraph(attributePaths = "roleSet")
	Optional<MemberEntity> findByMbEmail(String mbEmail);
    
    
    // 중복 체크
    boolean existsByMbId(String mbId);
    boolean existsByMbEmail(String mbEmail);

    // 회원 정보 조회
    Optional<MemberEntity> findByMbId(String mbId);
    
    // 페이징 처리된 모든 회원 조회
    Page<MemberEntity> findAll(Pageable pageable);
    
    
    // 아이디로 검색
    Page<MemberEntity> findByMbIdContaining(String keyword, Pageable pageable);

    // 이름으로 검색
    Page<MemberEntity> findByMbNameContaining(String keyword, Pageable pageable);
}