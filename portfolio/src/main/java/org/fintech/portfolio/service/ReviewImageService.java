package org.fintech.portfolio.service;

import java.util.List;

import org.fintech.portfolio.entity.ReviewImage;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewImageService {

    // 이미지를 업로드하고 ReviewImage 엔티티 저장
    void uploadImage(MultipartFile file, Long reNo);

    // 특정 리뷰의 이미지를 삭제
    void deleteImage(Long reNo, Long imageId);

    // 특정 리뷰의 이미지를 조회
    List<ReviewImage> getImagesByReview(Long rno);
    
}
