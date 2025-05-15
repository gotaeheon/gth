package org.fintech.portfolio.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.fintech.portfolio.dto.PlanDTO;
import org.fintech.portfolio.dto.PlanRatingDTO;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.repository.PlanRepository;
import org.fintech.portfolio.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final ReviewService reviewService;  // ReviewService 주입
    private final ReviewRepository reviewRepository;
    // 출발지랑 예산을 입력하면 그거에 맞는 여행지 추천
    @Override
    public List<PlanDTO> findPlansByRegionAndBudget(int departureRegionId, int budget) {
        String departureRegionName = mapRegionIdToName(departureRegionId);

        // 출발지 지역과 예산을 기준으로 여행 계획을 찾고, 숙소와 복귀 지역이 같은지 체크
        return planRepository.findByDepartureRegionNameAndTotalPriceLessThanEqual(
                departureRegionName, budget
        ).stream()
          .filter(plan -> plan.getHotelAddress().contains(plan.getArrivalRegionName()))  // 숙소 주소에 도착지 지역이 포함된 경우
          .map(PlanDTO::fromEntity)
          .collect(Collectors.toList());
    }

    private String mapRegionIdToName(int id) {
        return switch (id) {
            case 1 -> "서울";
            case 2 -> "부산";
            case 3 -> "대구";
            case 4 -> "인천";
            case 5 -> "광주";
            case 6 -> "대전";
            case 7 -> "울산";
            case 8 -> "세종";
            default -> throw new IllegalArgumentException("알 수 없는 지역 ID: " + id);
        };
    }
    
    // 추천여행조회
    @Override
    public Plan findById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 여행계획이 존재하지 않습니다. id=" + planId));
    }

    // 일정에 해당하는 후기를 삭제하는 메서드
    @Override
    public boolean deleteReviewsByPlanId(Long planId) {
        // ReviewService의 deleteReviewsByPlanId 메서드 호출
        return reviewService.deleteReviewsByPlanId(planId);
    }
    
    @Override
    public List<PlanRatingDTO> getSortedPlans(String sort) {
        List<Plan> plans = planRepository.findAll();

        List<PlanRatingDTO> result = plans.stream().map(plan -> {
            Double avgRating = reviewRepository.findAverageScoreByPlanId(plan.getPlanId());
            if (avgRating == null) avgRating = 0.0;
            int totalPrice = plan.getTotalPrice();
            return new PlanRatingDTO(plan, avgRating, totalPrice);
        }).collect(Collectors.toList());

        switch (sort) {
            case "priceAsc":
                result.sort(Comparator.comparing(PlanRatingDTO::getTotalPrice));
                break;
            case "priceDesc":
                result.sort(Comparator.comparing(PlanRatingDTO::getTotalPrice).reversed());
                break;
            case "ratingAsc":
                result.sort(Comparator.comparing(PlanRatingDTO::getAvgRating));
                break;
            default:
                result.sort(Comparator.comparing(PlanRatingDTO::getAvgRating).reversed());
        }

        return result;
    }
    
    // Plan을 DB에 저장
    public void savePlan(Plan plan) {
        planRepository.save(plan); 
    }
 
}
