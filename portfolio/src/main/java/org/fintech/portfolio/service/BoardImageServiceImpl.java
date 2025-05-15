package org.fintech.portfolio.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.entity.BoardImage;
import org.fintech.portfolio.repository.BoardImageRepository;
import org.fintech.portfolio.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardImageServiceImpl implements BoardImageService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;

    @Value("${spring.servlet.multipart.location}") // application.properties에 설정된 업로드 경로
    private String uploadDir;

    public BoardImageServiceImpl(BoardRepository boardRepository, BoardImageRepository boardImageRepository) {
        this.boardRepository = boardRepository;
        this.boardImageRepository = boardImageRepository;
    }

    // 이미지 업로드
    @Override
    public void uploadImage(MultipartFile file, Long bno) {
        try {
            String uuid = UUID.randomUUID().toString();
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName);

            // 확장자 검증
            if (!isValidImageExtension(extension)) {
                throw new IllegalArgumentException("유효하지 않은 파일 형식입니다.");
            }

            String filePath = uploadDir + "/" + uuid + "_" + originalFileName;
            file.transferTo(new File(filePath));

            BoardEntity boardEntity = boardRepository.findById(bno)
                    .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

            BoardImage boardImage = new BoardImage();
            boardImage.setUuid(uuid);
            boardImage.setFileName(originalFileName);
            boardImage.setFilePath(filePath);
            boardImage.setBoard(boardEntity);

            boardImageRepository.save(boardImage);

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // 이미지 삭제
    @Override
    public void deleteImage(Long bno, Long imageId) {
        BoardImage boardImage = boardImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));

        if (!boardImage.getBoard().getBno().equals(bno)) {
            throw new IllegalArgumentException("잘못된 게시물 번호입니다.");
        }

        deletePhysicalFile(boardImage.getFilePath());  // 파일 삭제
        boardImageRepository.delete(boardImage);       // DB 삭제
    }

    // 특정 게시물의 모든 이미지 삭제
    @Override
    public void deleteImageByBoardId(Long bno) {
        List<BoardImage> images = boardImageRepository.findByBoardBno(bno);

        for (BoardImage image : images) {
            deletePhysicalFile(image.getFilePath()); // 파일 삭제
            boardImageRepository.delete(image);      // DB 삭제
        }
    }

    // 게시물에 연결된 모든 이미지 조회
    @Override
    public List<BoardImage> getImagesByBoard(Long bno) {
        return boardImageRepository.findByBoardBno(bno);
    }

    // 파일 삭제
    private void deletePhysicalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new RuntimeException("파일 삭제 실패: " + filePath);
            }
        }
    }

    // 파일 확장자 추출
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1).toLowerCase();
    }

    // 유효한 이미지 확장자 검증
    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }
}
