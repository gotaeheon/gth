package org.fintech.portfolio.service;

import java.util.List;

import org.fintech.portfolio.dto.ReviewDTO;
import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.entity.Review;
import org.springframework.data.domain.Page;

public interface ReviewService {
    
    // 특정 여행 일정에 대한 리뷰 저장
	public Review saveReview(Long planId, Review review);
    
    // 리뷰 중복검사
    public boolean hasReviewedPlan(Long planId, String memberId);
    
    
    // 모든 리뷰 조회
    List<Review> getAllReviews();
    
    // 특정 여행 일정에 대한 리뷰 조회
    List<Review> getReviewsByPlanId(Long planId);
    
    // 특정 일정삭제시 같이 리뷰 삭제
    boolean deleteReviewsByPlanId(Long planId);
    
    // 특정 여행 일정에 대한 리뷰 조회 (DTO 반환)
    ReviewDTO findByPlanId(Long planId);
    
    // 특정 리뷰 상세 보기 및 조회수 증가
    Review viewReview(Long reviewId);
    
    // 특정 리뷰 좋아요 수 증가
    Review likeReview(Long reviewId);
    
    // 로그인한 사용자의 리뷰만 가져오기
    List<Review> getReviewsByMemberId(String mbId);
    
    // 리뷰 삭제 
    public void deleteReview(Long reviewNo);
    
    // 평점 높은 리뷰 가져오기
    List<Review> getReviewsSortedByScore();
    
    // 리뷰 조회
    ReviewDTO getReview(Long rno);
    
    // 리뷰 수정
    void update(Long rno, ReviewDTO reviewDTO);
    
    // 페이징 처리    
    public Page<Review> getReviewList(int page, int size, String searchType, String keyword, String sort);
}
