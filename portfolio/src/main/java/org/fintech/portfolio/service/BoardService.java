package org.fintech.portfolio.service;

import java.util.List;
import org.fintech.portfolio.dto.BoardDTO;
import org.fintech.portfolio.entity.BoardEntity;
import org.springframework.data.domain.Page;

public interface BoardService {

    // 게시물 조회 (조회수 증가)
    BoardEntity viewBoard(Long bno);

    // 게시물 좋아요 증가
    BoardEntity likeBoard(Long bno);

    // 새 게시물 생성
    BoardEntity createBoard(BoardEntity board);

    // 게시물 삭제
    void deleteBoard(Long bno);

    // 모든 게시물 조회
    List<BoardEntity> getAllBoards();

    // 게시물 수정
    void update(Long bno, BoardDTO boardDTO);

    // 게시물 상세 조회 (DTO 반환)
    BoardDTO findById(Long bno);

    // 인기 게시물 조회
    List<BoardEntity> getTopLikedBoards();
    
    // 게시판 목록 조회 (페이징)
    public Page<BoardEntity> getBoardList(int page, int size);
    
    // 게시판 검색조건
    Page<BoardEntity> searchBoards(int page, int size, String searchType, String keyword);
}
