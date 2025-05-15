package org.fintech.portfolio.repository;

import java.util.List;

import org.fintech.portfolio.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    // 추천일정보기
    List<Plan> findByDepartureRegionNameAndTotalPriceLessThanEqual(String departureRegionName, int budget);
    
    // 모든 일정 조회
    List<Plan> findAll(); 
    
    // 출발지로 검색
    Page<Plan> findByDepartureRegionNameContaining(String keyword, Pageable pageable);

    // 도착지로 검색
    Page<Plan> findByArrivalRegionNameContaining(String keyword, Pageable pageable);
    
    // 호텔이름으로 검색
    Page<Plan> findByHotelNameContaining(String keyword, Pageable pageable);
}
