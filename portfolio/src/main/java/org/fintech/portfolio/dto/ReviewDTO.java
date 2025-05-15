package org.fintech.portfolio.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.fintech.portfolio.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	
	private Long rno; //리뷰번호
    private String mbId; //작성자
    private String reTitle; // 제목
    private String reComment; // 내용
    private int reScore; // 평점
    private int reHit; // 조회수
    private int reLike; // 좋아욘
    private Long planId;  // 리뷰가 작성되는 여행 계획의 ID
    private LocalDateTime reUpdate; // 수정일 추가
    //p640 특정 게시물의 첨부파일 목록
  	private List<String> fileNames;
  	
    // Review 객체를 매개변수로 받는 생성자 추가
 // Review 객체를 매개변수로 받는 생성자 추가
    public ReviewDTO(Review review) {
    	this.rno = review.getReNo();
        this.mbId = review.getMbId();
        this.reTitle = review.getReTitle();
        this.reComment = review.getReComment();
        this.reScore = review.getReScore();
        this.planId = review.getPlan().getPlanId();  // Plan 객체에서 planId를 추출
        this.reUpdate = review.getReUpdate();
    }
}
