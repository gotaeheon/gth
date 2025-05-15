package org.fintech.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.fintech.portfolio.dto.ReviewDTO;
import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.entity.Review;
import org.fintech.portfolio.repository.PlanRepository;
import org.fintech.portfolio.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlanRepository planRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, PlanRepository planRepository) {
        this.reviewRepository = reviewRepository;
        this.planRepository = planRepository;
    }

    // 특정 여행 일정에 리뷰 저장
    @Override
    public Review saveReview(Long planId, Review review) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 계획이 존재하지 않습니다. planId=" + planId));

        review.setPlan(plan);
        return reviewRepository.save(review);
    }
    
    // 리뷰 중복검사
    // 사용자가 이미 리뷰를 작성했는지 확인
    public boolean hasReviewedPlan(Long planId, String memberId) {
        return reviewRepository.existsByPlan_PlanIdAndMbId(planId, memberId);
    }
    
    // 전체 리뷰 목록 반환
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // 특정 여행 일정에 연결된 리뷰 목록 반환
    @Override
    public List<Review> getReviewsByPlanId(Long planId) {
        return reviewRepository.findByPlan_PlanId(planId);
    }

    // 특정 여행 계획에 대한 후기를 삭제
    @Override
    public boolean deleteReviewsByPlanId(Long planId) {
        try {
            reviewRepository.deleteByPlan_PlanId(planId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // 리뷰 상세 조회 + 조회수 증가
    @Transactional
    @Override
    public Review viewReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        review.setReHit(review.getReHit() + 1); // 조회수 +1
        return reviewRepository.save(review);  // 저장 후 반환
    }

    // 좋아요 수 증가
    @Transactional
    @Override
    public Review likeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        review.setReLike(review.getReLike() + 1); // 좋아요 수 +1
        return reviewRepository.save(review);     // 저장 후 반환
    }

    // 특정 여행 일정에 대한 리뷰를 DTO로 변환해 반환
    @Override
    public ReviewDTO findByPlanId(Long planId) {
        Review review = reviewRepository.findByPlan_PlanId(planId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 여행 일정에 대한 리뷰가 없습니다."));

        return new ReviewDTO(review); // DTO 변환 후 반환
    }

    // 로그인한 사용자의 리뷰 목록 조회
    @Override
    public List<Review> getReviewsByMemberId(String mbId) {
        return reviewRepository.findByMbId(mbId);
    }
    
    // 리뷰 삭제 메서드
    public void deleteReview(Long reviewNo) {
        reviewRepository.deleteById(reviewNo);  // 리뷰를 ID로 찾아 삭제
    }
   
    @Override
    public List<Review> getReviewsSortedByScore() {
        return reviewRepository.findAllByOrderByReScoreDesc();
    }

    @Override
    public ReviewDTO getReview(Long rno) {
        // 리뷰를 ID로 조회
        Review review = reviewRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        // 조회된 Review 엔티티를 ReviewDTO로 변환하여 반환
        return new ReviewDTO(review);
    }
    		
    @Override
    public void update(Long reviewNo, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewNo)
            .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));

        // DTO에서 가져온 값으로 갱신
        review.setReTitle(reviewDTO.getReTitle());
        review.setReComment(reviewDTO.getReComment());
        review.setReScore(reviewDTO.getReScore()); // 평점도 포함되어 있다면

        review.setReUpdate(LocalDateTime.now()); // 수정일 갱신 (필드가 있을 경우)

        reviewRepository.save(review);
    }

    // 페이징처리+ 검색조건
    @Override
    public Page<Review> getReviewList(int page, int size, String searchType, String keyword, String sort) {
        Pageable pageable;

        // 평점 순 정렬 처리
        if ("reScore".equals(sort)) {
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "reScore"));
        } else if ("reLike".equals(sort)) {
            // 좋아요순 정렬
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "reLike"));
        } else if ("reRegdate".equals(sort)) {
            // 작성일순 정렬
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "reRegdate"));
        } else {
            // 기본 정렬: reNo
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "reNo"));
        }

        if (searchType == null || keyword == null || keyword.isEmpty()) {
            return reviewRepository.findAll(pageable);
        }

        switch (searchType) {
            case "reTitle":
                return reviewRepository.findByReTitleContaining(keyword, pageable);
            case "reComment":  // 오타 수정: reConment -> reComment
                return reviewRepository.findByReCommentContaining(keyword, pageable);
            case "mbId":
                return reviewRepository.findByMbIdContaining(keyword, pageable);
            case "reTitle_reComment":
                return reviewRepository.findByReTitleContainingOrReCommentContaining(keyword, keyword, pageable);
            default:
                return reviewRepository.findAll(pageable);
        }
    }
}
