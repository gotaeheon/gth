package org.fintech.portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDTO {
	
	private Long commentId; // 댓글 번호
    private Long bno;           // 자유게시판 글 번호
    private String mbId;        // 작성자
    private String content; // 내용
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
}
