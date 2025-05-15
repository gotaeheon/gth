package org.fintech.portfolio.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "re_no")
    private Long reNo;  // 리뷰 번호

    @Column(name = "mb_id")
    private String mbId;  // 회원 ID

    @Column(name = "re_title")
    private String reTitle;  // 제목

    @Column(name = "re_conment")
    private String reComment;  // 내용

    @Column(name = "re_hit")
    private int reHit;  // 조회수

    @Column(name = "re_like")
    private int reLike;  // 좋아요

    @Column(name = "re_score")   
    private int reScore;  // 평점
    
    @CreationTimestamp 
    @Column(name = "re_regdate")
    private LocalDateTime reRegdate;  // 등록일
    
    @UpdateTimestamp
    @Column(name = "re_update")
    private LocalDateTime reUpdate;  // 수정일
    
    @Transient // DB에 저장되지 않는 임시 필드
    private String thumbnailFileName;
    // Review와 여러 이미지를 연결
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ReviewImage> imageSet = new HashSet<>();  // 여러 이미지가 리뷰에 연결

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", referencedColumnName = "plan_id")
    private Plan plan;
}
