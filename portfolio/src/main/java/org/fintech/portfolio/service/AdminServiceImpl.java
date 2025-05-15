package org.fintech.portfolio.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.fintech.portfolio.dto.PlanRatingDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.entity.Review;
import org.fintech.portfolio.repository.AdminRepository;
import org.fintech.portfolio.repository.BoardRepository;
import org.fintech.portfolio.repository.MemberRepository;
import org.fintech.portfolio.repository.PlanRepository;
import org.fintech.portfolio.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  // 생성자를 자동으로 생성하여 의존성 주입을 처리
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;  // AdminRepository 의존성 주입
    private final PlanRepository planRepository;
    private final ReviewRepository reviewRepository;    
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    
    // 주어진 Plan ID에 대해 평균 평점을 반환하는 메서드
    @Override
    public Double getAverageRatingByPlanId(Long planId) {
        // AdminRepository에서 Plan ID에 해당하는 평균 평점을 가져오는 쿼리 호출
        return adminRepository.findAverageRatingByPlanId(planId);
    }

    // 주어진 Plan ID에 대한 모든 리뷰를 반환하는 메서드
    @Override
    public List<Review> getReviewsByPlanId(Long planId) {
        // AdminRepository에서 Plan ID에 해당하는 모든 리뷰를 조회하는 쿼리 호출
        return adminRepository.findReviewsByPlanId(planId);
    }

    // 모든 리뷰를 평점 순으로 정렬하여 반환하는 메서드
    @Override
    public List<Review> getReviewsSortedByRating() {
        // AdminRepository에서 평점 순으로 정렬된 모든 리뷰를 조회하는 쿼리 호출
        return adminRepository.findAllReviewsSortedByRating();
    }

    // Plan ID에 해당하는 모든 리뷰의 평점의 평균을 계산하여 반환하는 메서드
    public Double getTotalAverageRating(Long planId) {
        // 주어진 planId에 대한 리뷰 목록을 가져온다
        List<Review> reviews = adminRepository.findReviewsByPlanId(planId);
        
        // 리뷰가 없을 경우 평균 평점은 0으로 반환
        if (reviews.isEmpty()) {
            return 0.0;
        }

        // 리뷰의 총 평점을 합산
        double totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getReScore();
        }

        // 총 평점을 리뷰의 수로 나누어 평균을 계산
        return totalRating / reviews.size();
    }
    
    
    // 모든 추천일정 찾기 
    @Override
    public List<Plan> findAllPlans() {
        return planRepository.findAll();
    }
    
    //검색조건 추가
    @Override
    public List<PlanRatingDTO> getSortedPlans(String sort) {
        List<Plan> plans = planRepository.findAll();

        List<PlanRatingDTO> result = plans.stream().map(plan -> {
            Double avgRating = reviewRepository.findAverageScoreByPlanId(plan.getPlanId());
            if (avgRating == null) avgRating = 0.0;
            int totalPrice = plan.getTotalPrice();  // 가격 가져오기
            return new PlanRatingDTO(plan, avgRating, totalPrice);  // 생성자에 가격 포함
        }).collect(Collectors.toList());

        // 정렬 조건
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

            default: // ratingDesc
                result.sort(Comparator.comparing(PlanRatingDTO::getAvgRating).reversed());
        }

        return result;
    }
    
    // 회원 목록 조회 (페이징)
    public Page<MemberEntity> getMemberList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("mbUpdate")));  // regDate 기준 내림차순 정렬
        return memberRepository.findAll(pageable);  // 이 메서드에서 페이징된 최신순 게시물 리스트를 반환
    }
    
    // 회원 목록 조회 (페이징+ 검색조건)
    @Override
    public Page<MemberEntity> getMemberList(int page, int size, String searchType, String keyword) {
        Pageable pageable;

        pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "mbUpdate"));

        if (searchType == null || keyword == null || keyword.isEmpty()) {
            return memberRepository.findAll(pageable);
        }

        switch (searchType) {
            case "MbId":
                return memberRepository.findByMbIdContaining(keyword, pageable);
            case "MbName":
                return memberRepository.findByMbNameContaining(keyword, pageable);
            default:
                return memberRepository.findAll(pageable);
        }
    }
    
    // 추천일정 목록 조회 (페이징)
    public Page<Plan> findPlansWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("planId"))); 
        return planRepository.findAll(pageable);  // 이 메서드에서 페이징된 최신순 게시물 리스트를 반환
    }
	
    // 추천일정 목록 조회 (페이징+ 검색조건)
    @Override
    public Page<Plan> findPlansWithPaging(int page, int size, String searchType, String keyword) {
        Pageable pageable;

        pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "planId"));

        if (searchType == null || keyword == null || keyword.isEmpty()) {
            return planRepository.findAll(pageable);
        }

        switch (searchType) {
            case "DepartureRegionName":
                return planRepository.findByDepartureRegionNameContaining(keyword, pageable);
            case "ArrivalRegionName":
                return planRepository.findByArrivalRegionNameContaining(keyword, pageable);
            case "HotelName":
                return planRepository.findByHotelNameContaining(keyword, pageable);
            default:
                return planRepository.findAll(pageable);
        }
    }
}
