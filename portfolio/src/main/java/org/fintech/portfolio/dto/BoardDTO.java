package org.fintech.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.fintech.portfolio.entity.BoardEntity;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long bno; // 게시물 번호
    private String title; // 게시물 제목
    private String content; // 게시물 내용
    private String writer; // 작성자
    private int viewCount; // 조회수
    private int likeCount; // 좋아요 수
    private LocalDateTime regDate; // 등록일
    private LocalDateTime modDate; //수정일

    //Entity(Board) -> DTO로 변환하는 메서드
    public static BoardDTO fromEntity(BoardEntity board) {
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .regDate(board.getRegDate())
                .modDate(board.getModDate()) // modDate 추가
                .build();
    }
}
