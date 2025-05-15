package org.fintech.portfolio.service;

import java.util.List;
import org.fintech.portfolio.entity.BoardImage;
import org.springframework.web.multipart.MultipartFile;

public interface BoardImageService {

    // 이미지 업로드
    void uploadImage(MultipartFile file, Long bno);

    // 이미지 삭제 (개별)
    void deleteImage(Long bno, Long imageId);

    // 특정 게시물의 모든 이미지 삭제
    void deleteImageByBoardId(Long bno);

    // 특정 게시물의 이미지 조회
    List<BoardImage> getImagesByBoard(Long bno);
}
