package org.fintech.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.fintech.portfolio.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // 게시물 번호로 게시물 조회
    Optional<BoardEntity> findByBno(Long bno);
    
    // 조회수 증가
    @Modifying
    @Query("UPDATE BoardEntity b SET b.viewCount = b.viewCount + 1 WHERE b.bno = :bno")
    void incrementViewCount(@Param("bno") Long bno);

    // 좋아요 수 증가
    @Modifying
    @Query("UPDATE BoardEntity b SET b.likeCount = b.likeCount + 1 WHERE b.bno = :bno")
    void incrementLikeCount(@Param("bno") Long bno);
    
    // 좋아요 수가 많은 게시물 상위 4개 조회
    List<BoardEntity> findTop4ByOrderByLikeCountDesc();
    
 // 페이징 처리된 모든 게시물 조회
    Page<BoardEntity> findAll(Pageable pageable);
    
    Page<BoardEntity> findByTitleContaining(String title, Pageable pageable);
    Page<BoardEntity> findByContentContaining(String content, Pageable pageable);
    Page<BoardEntity> findByWriterContaining(String writer, Pageable pageable);
    Page<BoardEntity> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

}

