package org.fintech.portfolio.repository;

import org.fintech.portfolio.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Review, Long> {

    // 특정 추천일정의 평균 평점 조회
    @Query("SELECT AVG(r.reScore) FROM Review r WHERE r.plan.planId = :planId")
    Double findAverageRatingByPlanId(@Param("planId") Long planId);
    
    // 특정 아이디로 게시물 조회
    @Query("SELECT r FROM Review r WHERE r.plan.planId = :planId")
    List<Review> findReviewsByPlanId(@Param("planId") Long planId);

    // 리뷰를 평점순으로 조회
    @Query("SELECT r FROM Review r ORDER BY r.reScore DESC")
    List<Review> findAllReviewsSortedByRating();
    
}
