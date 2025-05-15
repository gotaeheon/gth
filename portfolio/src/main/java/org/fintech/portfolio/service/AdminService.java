package org.fintech.portfolio.service;

import java.util.List;

import org.fintech.portfolio.dto.PlanRatingDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.entity.Review;
import org.springframework.data.domain.Page;

public interface AdminService {

    // 특정 추천일정에 대한 평균 평점 계산
    Double getAverageRatingByPlanId(Long planId);

    // 특정 추천일정에 대한 모든 리뷰 조회
    List<Review> getReviewsByPlanId(Long planId);

    // 모든 리뷰와 추천일정을 평점순으로 조회
    List<Review> getReviewsSortedByRating();
    
    public Double getTotalAverageRating(Long planId);
    
    public List<Plan> findAllPlans();
    
    public List<PlanRatingDTO> getSortedPlans(String sort);	
    
    // 회원 목록 조회 (페이징)
    public Page<MemberEntity> getMemberList (int page, int size);
    
    // 전체회원 목록 조회 (페이징 + 검색조건)
    Page<MemberEntity> getMemberList(int page, int size, String searchType, String keyword);
    
    // 전체추천일정 목록 조회 (페이징 + 검색조건)
	public Page<Plan> findPlansWithPaging(int page, int size, String searchType, String keyword);
}
