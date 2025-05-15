package org.fintech.portfolio.service;

import java.util.List;

import org.fintech.portfolio.dto.PlanDTO;
import org.fintech.portfolio.dto.PlanRatingDTO;
import org.fintech.portfolio.entity.Plan;

public interface PlanService {
	
	// 추천 일정 찾기
	List<PlanDTO> findPlansByRegionAndBudget(int departureRegionId, int budget);
	
	//여행 계획의 ID를 이용해 상세 정보를 조회
	Plan findById(Long planId);
	
	// 일정에 해당하는 후기를 삭제
    public boolean deleteReviewsByPlanId(Long planId);
    
    // 별점순 + 가격순 정렬
    List<PlanRatingDTO> getSortedPlans(String sort); 
    
    //추천일정 저장
    public void savePlan(Plan plan);
    
 
}
