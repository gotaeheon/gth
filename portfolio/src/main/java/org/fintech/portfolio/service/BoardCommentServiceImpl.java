package org.fintech.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.fintech.portfolio.entity.BoardComment;
import org.fintech.portfolio.repository.BoardCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentRepository commentRepository;

    // 댓글을 저장하는 메소드
    @Override
    public BoardComment saveComment(BoardComment comment) {
        return commentRepository.save(comment); // 댓글을 데이터베이스에 저장하고 저장된 댓글을 반환
    }

    // 특정 게시물(bno)의 모든 댓글을 조회하는 메소드
    @Override
    public List<BoardComment> getCommentsByBno(Long bno) {
        return commentRepository.findByBno(bno); // 주어진 게시물 번호에 해당하는 모든 댓글을 조회하여 반환
    }

    // 댓글을 삭제하는 메소드
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId); // 주어진 댓글 ID로 댓글을 삭제
    }

    // 댓글을 업데이트하는 메소드
    @Override
    public BoardComment updateComment(Long commentId, String newContent) {
        Optional<BoardComment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            BoardComment comment = optionalComment.get();
            comment.setContent(newContent); // 댓글 내용을 새로 설정
            return commentRepository.save(comment); // 업데이트된 댓글을 저장하고 반환
        }
        throw new RuntimeException("댓글을 찾을 수 없습니다."); // 댓글이 없으면 예외 발생
    }
}
