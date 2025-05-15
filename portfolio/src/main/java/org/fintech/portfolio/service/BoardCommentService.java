package org.fintech.portfolio.service;

import org.fintech.portfolio.entity.BoardComment;

import java.util.List;

public interface BoardCommentService {

    // 새로운 댓글을 저장하는 메소드
    BoardComment saveComment(BoardComment comment);

    // 게시물 번호(bno)에 해당하는 모든 댓글을 조회하여 반환
    List<BoardComment> getCommentsByBno(Long bno);

    // 댓글을 삭제
    void deleteComment(Long commentId);

    // 댓글을 업데이트
    BoardComment updateComment(Long commentId, String newContent);
}
