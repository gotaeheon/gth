package org.fintech.portfolio.repository;

import java.util.List;

import org.fintech.portfolio.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
	
    // 특정 리부에 대한 이미지 목록 조회
	List<ReviewImage> findByReviewReNo(Long reNo);
}
