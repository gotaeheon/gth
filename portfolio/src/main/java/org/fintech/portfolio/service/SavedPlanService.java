package org.fintech.portfolio.service;

import java.util.List;
import java.util.Optional;

import org.fintech.portfolio.dto.SavedPlanDTO;
import org.fintech.portfolio.entity.SavedPlan;

public interface SavedPlanService {
	
	// 사용자가 계획을 저장하는 메서드
    void savePlanForMember(String memberId, Long planId); 
    
    // 사용자가 저장한 계획 볼수있는 메서드
    //List<SavedPlan> getSavedPlansByMemberId(String memberId);
    
    // 마이페이지용으로 필요한 상세 정보 DTO 목록
    List<SavedPlanDTO> getSavedPlanDTOsByMemberId(String memberId);
    
    // 저장된 일정과 삭제하는 메서드
    public void deleteSavedPlan(Long saveId);
    
    // 저장된 일정 조회 메서드 (findById 추가)
    Optional<SavedPlan> findById(Long saveId);

}