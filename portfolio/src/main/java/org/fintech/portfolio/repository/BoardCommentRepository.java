package org.fintech.portfolio.repository;

import org.fintech.portfolio.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
	
	// 특정 게시물의 댓글 가져오기
    List<BoardComment> findByBno(Long bno); 
}
