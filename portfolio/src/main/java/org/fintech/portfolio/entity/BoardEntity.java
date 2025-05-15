package org.fintech.portfolio.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 날짜 자동 설정을 위한 설정
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; // 게시물 번호

    @Column(nullable = false, length = 200)
    private String title; // 게시물 제목

    @Column(nullable = false, length = 2000)
    private String content; // 게시물 내용

    @Column(nullable = false, length = 50)
    private String writer; // 작성자

    @Column(nullable = false)
    private int viewCount = 0; // 조회수

    @Column(nullable = false)
    private int likeCount = 0; // 좋아요 수

    @CreatedDate
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate; // 등록일자

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime modDate; // 수정일자
    
    @Transient // DB에 저장되지 않는 임시 필드
    private String thumbnailFileName;

    // 1:N 관계 설정: 하나의 게시물에는 여러 개의 이미지가 연결될 수 있음
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<BoardImage> imageSet = new HashSet<>(); // 게시물에 첨부된 이미지들

    public void increaseViewCount() {  // 조회수를 1 증가시키는 메서드
        this.viewCount++;
    }

    public void increaseLikeCount() {  // 좋아요 수를 1 증가시키는 메서드
        this.likeCount++;
    }

    public void decreaseLikeCount() {  // 좋아요가 0 미만으로 떨어지지 않게 감소시키는 메서드
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
    
    public void setThumbnailFileName(String thumbnailFileName) {
        this.thumbnailFileName = thumbnailFileName;
    }

    // 첨부파일(이미지)을 추가하는 메서드
    public void addImage(String uuid, String fileName, String filePath) {
        // 새로운 BoardImage 객체를 생성
        BoardImage boardImage = new BoardImage();
        boardImage.setUuid(uuid); // UUID를 설정
        boardImage.setFileName(fileName); // 파일명을 설정
        boardImage.setFilePath(filePath); // 파일 경로를 설정
        boardImage.setBoard(this); // 현재 게시물(BoardEntity)과 연결
        boardImage.setOrd(imageSet.size()); // 이미지 순서를 설정 (현재 이미지 세트의 크기)

        imageSet.add(boardImage); // Set에 이미지 추가
    }
}
