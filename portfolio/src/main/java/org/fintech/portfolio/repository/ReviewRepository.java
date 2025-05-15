package org.fintech.portfolio.repository;

import java.util.List;

import org.fintech.portfolio.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 여행 계획에 대한 리뷰를 조회
	// Plan의 id를 통해 리뷰를 조회
    List<Review> findByPlan_PlanId(Long planId); 

    // 특정 회원(mbId)이 작성한 모든 리뷰 조회
    List<Review> findByMbId(String mbId);
    
    // 특정 계획에 대해 해당 회원이 리뷰를 작성했는지 확인하는 메소드
    boolean existsByPlan_PlanIdAndMbId(Long planId, String mbId);
    
    // 특정 계획(planId)에 해당하는 모든 리뷰 삭제
    void deleteByPlan_PlanId(Long planId);  // planId를 정확히 지정
    
    // 평점(reScore) 순으로 모든 리뷰 조회 (내림차순)
    List<Review> findAllByOrderByReScoreDesc();
    
    // 'Review' 엔티티에서 주어진 planId에 해당하는 모든 리뷰의 평균 평점을 조회하는 쿼리 메소드
    @Query("SELECT AVG(r.reScore) FROM Review r WHERE r.plan.planId = :planId")
    Double findAverageScoreByPlanId(@Param("planId") Long planId);
    
    // 평점 순으로 모든 리뷰를 조회 (Pageable 적용)
    Page<Review> findAllByOrderByReScoreDesc(Pageable pageable);

    // 페이징 처리된 모든 게시물 조회
    Page<Review> findAll(Pageable pageable);

    // 기존 검색 메소드들
    Page<Review> findByReTitleContaining(String keyword, Pageable pageable);
    Page<Review> findByReCommentContaining(String keyword, Pageable pageable);
    Page<Review> findByMbIdContaining(String keyword, Pageable pageable);
    Page<Review> findByReTitleContainingOrReCommentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}
