package org.fintech.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.fintech.portfolio.dto.SavedPlanDTO;
import org.fintech.portfolio.entity.SavedPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedPlanRepository extends JpaRepository<SavedPlan, Long> {

    // 특정 회원이 이미 해당 planId를 저장했는지 확인
    Optional<SavedPlan> findByMemberIdAndPlanId(String memberId, Long planId);

    // 특정 회원이 저장한 모든 일정 DTO로 조회
    @Query("SELECT new org.fintech.portfolio.dto.SavedPlanDTO(sp.saveId, sp.planId, p.departureRegionName, p.arrivalRegionName, p.hotelName, p.totalPrice, sp.savedAt) " +
    	       "FROM SavedPlan sp JOIN Plan p ON sp.planId = p.planId " +
    	       "WHERE sp.memberId = :memberId")
    	List<SavedPlanDTO> findSavedPlansByMemberId(@Param("memberId") String memberId);

    // 특정 회원이 저장한 SavedPlan 엔티티 전체 조회
    List<SavedPlan> findByMemberId(String memberId);
}
