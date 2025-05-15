package org.fintech.portfolio.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.fintech.portfolio.entity.Review;
import org.fintech.portfolio.entity.ReviewImage;
import org.fintech.portfolio.repository.ReviewImageRepository;
import org.fintech.portfolio.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReviewImageServiceImpl implements ReviewImageService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Value("${spring.servlet.multipart.location}") // application.properties에서 업로드 디렉토리 가져오기
    private String uploadDir;

    public ReviewImageServiceImpl(ReviewRepository reviewRepository, ReviewImageRepository reviewImageRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
    }
    
    // 이미지 업로드
    @Override
    public void uploadImage(MultipartFile file, Long reviewId) {
        try {
            String uuid = UUID.randomUUID().toString();
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName);
            
            // 확장자 검증
            if (!isValidImageExtension(extension)) {
                throw new IllegalArgumentException("유효하지 않은 파일 형식입니다.");
            }

            // 실제 서버 저장 경로
            String filePath = uploadDir + "/" + uuid + "_" + originalFileName;
            file.transferTo(new File(filePath));

            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setUuid(uuid);
            reviewImage.setFileName(originalFileName);
            reviewImage.setFilePath(filePath);
            reviewImage.setReview(review);
            
            reviewImageRepository.save(reviewImage);

        } catch (IOException e) {
        	
            throw new RuntimeException("파일 업로드 실패", e);
            
        } catch (IllegalArgumentException e) {
        	
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(Long reviewId, Long imageId) {
        // 이미지 삭제 처리
        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));

        if (!reviewImage.getReview().getReNo().equals(reviewId)) {
            throw new IllegalArgumentException("잘못된 리뷰 번호입니다.");
        }

        // 파일 시스템에서 이미지 삭제
        File file = new File(uploadDir + "/images", reviewImage.getFilePath().substring(7)); // "images/" 이후 경로만 사용
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new RuntimeException("이미지 파일 삭제 실패");
            }
        }

        // ReviewImage 엔티티 삭제
        reviewImageRepository.delete(reviewImage);
    }
   

    @Override
    public List<ReviewImage> getImagesByReview(Long reviewId) {
        // 특정 리뷰에 속한 이미지들 반환
        return reviewImageRepository.findByReviewReNo(reviewId);
    }

    // 파일 확장자 추출
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1).toLowerCase();
    }

    // 유효한 이미지 확장자 검증
    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }
}
