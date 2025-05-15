package org.fintech.portfolio.repository;

import org.fintech.portfolio.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    
	// 특정 게시물에 대한 이미지 목록 조회
    List<BoardImage> findByBoardBno(Long bno);
}
