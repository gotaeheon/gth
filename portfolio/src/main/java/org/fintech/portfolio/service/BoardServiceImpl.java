package org.fintech.portfolio.service;

import java.util.List;

import org.fintech.portfolio.dto.BoardDTO;
import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시물 조회 (조회수 증가)
    @Transactional
    @Override
    public BoardEntity viewBoard(Long bno) {
        BoardEntity board = boardRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 존재하지 않습니다."));
        
        boardRepository.incrementViewCount(bno);  // 조회수 증가
        return board;
    }

    // 게시물 좋아요 증가
    @Transactional
    @Override
    public BoardEntity likeBoard(Long bno) {
        BoardEntity board = boardRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 존재하지 않습니다."));
        
        boardRepository.incrementLikeCount(bno);  // 좋아요 수 증가
        return board;
    }

    // 새 게시물 생성
    @Override
    public BoardEntity createBoard(BoardEntity board) {
        return boardRepository.save(board);
    }

    // 게시물 삭제
    @Override
    public void deleteBoard(Long bno) {
        boardRepository.deleteById(bno);
    }

    // 모든 게시물 조회
    @Override
    public List<BoardEntity> getAllBoards() {
        return boardRepository.findAll();
    }

    // 게시물 수정
    @Override
    public void update(Long bno, BoardDTO boardDTO) {
        BoardEntity board = boardRepository.findById(bno)
            .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));
        
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);  // 수정된 게시물 저장
    }

    // 게시물 상세 조회 (DTO 반환)
    @Override
    public BoardDTO findById(Long bno) {
        BoardEntity boardEntity = boardRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
        
        return new BoardDTO(
            boardEntity.getBno(),
            boardEntity.getTitle(),
            boardEntity.getContent(),
            boardEntity.getWriter(),
            boardEntity.getViewCount(),
            boardEntity.getLikeCount(),
            boardEntity.getRegDate(),
            boardEntity.getModDate()
        );
    }

    // 인기 게시물 조회
    @Override
    public List<BoardEntity> getTopLikedBoards() {
        return boardRepository.findTop4ByOrderByLikeCountDesc();
    }
    
    // 게시판 목록 조회 (페이징)
    public Page<BoardEntity> getBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("regDate")));  // regDate 기준 내림차순 정렬
        return boardRepository.findAll(pageable);  // 이 메서드에서 페이징된 최신순 게시물 리스트를 반환
    }
    
    // 게시판 목록 조회 (페이징+ 검색조건)
    @Override
    public Page<BoardEntity> searchBoards(int page, int size, String searchType, String keyword) {
        Pageable pageable;

        if ("like".equals(searchType)) {
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "likeCount"));
            return boardRepository.findAll(pageable);
        }

        pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "bno"));

        if (searchType == null || keyword == null || keyword.isEmpty()) {
            return boardRepository.findAll(pageable);
        }

        switch (searchType) {
            case "title":
                return boardRepository.findByTitleContaining(keyword, pageable);
            case "content":
                return boardRepository.findByContentContaining(keyword, pageable);
            case "writer":
                return boardRepository.findByWriterContaining(keyword, pageable);
            case "title_content":
                return boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            default:
                return boardRepository.findAll(pageable);
        }
    }
}
