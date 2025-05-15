package org.fintech.portfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImage {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId; // 이미지 번호

    @Column(nullable = false, length = 255)
    private String uuid; // 파일 UUID (중복 방지 및 고유 식별자)

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path",nullable = false, length = 255)
    private String filePath; // 파일 저장 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "re_no")
    private Review review;  // 리뷰 엔티티와 연관
    
    private int ord; // 이미지 순서 (게시물에서 이미지의 순서 관리)
    
}
