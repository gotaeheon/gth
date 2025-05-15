package org.fintech.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.fintech.portfolio.dto.SavedPlanDTO;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.entity.SavedPlan;
import org.fintech.portfolio.repository.PlanRepository;
import org.fintech.portfolio.repository.ReviewRepository;
import org.fintech.portfolio.repository.SavedPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class SavedPlanServiceImpl implements SavedPlanService {

    @Autowired
    private SavedPlanRepository savedPlanRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private PlanRepository planRepository;
    
    
    @Override
    public Optional<SavedPlan> findById(Long saveId) {
        // SavedPlanRepository에서 해당 saveId를 찾아 반환
        return savedPlanRepository.findById(saveId);
    }
    
    
    // 사용자가 계획을 마이페이지에 저장하는 로직
    @Override
    public void savePlanForMember(String memberId, Long planId) {
        // 이미 저장했는지 확인
        Optional<SavedPlan> existingPlan = savedPlanRepository.findByMemberIdAndPlanId(memberId, planId);

        if (existingPlan.isPresent()) {
            // 이미 저장된 경우 "이미 추가된 일정입니다" 메시지를 설정
            throw new IllegalArgumentException("이미 추가된 일정입니다.");
        }

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        SavedPlan savedPlan = new SavedPlan();
        savedPlan.setMemberId(memberId);
        savedPlan.setPlanId(planId);
        savedPlan.setSavedAt(LocalDateTime.now());
        savedPlan.setDepartureRegionName(plan.getDepartureRegionName());  // 출발지 이름 추가
        savedPlan.setArrivalRegionName(plan.getArrivalRegionName());  // 도착지 이름 추가

        savedPlanRepository.save(savedPlan);
    }

    // 저장된 일정과 해당 리뷰를 삭제하는 메서드
    @Transactional
    public void deleteSavedPlan(Long saveId) {
        // 저장된 일정 가져오기
        SavedPlan savedPlan = savedPlanRepository.findById(saveId)
        		
            .orElseThrow(() -> new IllegalArgumentException("저장된 일정을 찾을 수 없습니다."));
        
        Long planId = savedPlan.getPlanId();


        // 저장된 일정 삭제
        savedPlanRepository.delete(savedPlan);
    }

    // 마이페이지용 DTO 목록 조회 - JPQL 쿼리 사용
    @Override
    public List<SavedPlanDTO> getSavedPlanDTOsByMemberId(String memberId) {
        return savedPlanRepository.findSavedPlansByMemberId(memberId);
    }

}
