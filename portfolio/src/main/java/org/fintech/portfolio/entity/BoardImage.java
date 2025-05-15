package org.fintech.portfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "board_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {

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
    @JoinColumn(name = "bno") // 외래 키로 게시물 번호 참조
    private BoardEntity board; // 게시물과의 관계 (다대일 관계)

    private int ord; // 이미지 순서 (게시물에서 이미지의 순서 관리)
}
