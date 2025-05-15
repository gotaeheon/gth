package org.fintech.portfolio.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageService {

    @Value("${spring.servlet.multipart.location}") // 업로드 디렉토리 경로 가져오기
    private String uploadDir;

    public File createThumbnail(File originalImage) throws IOException {
        // 썸네일 파일 경로 (원본 파일 경로에서 "_thumb" 붙여서 저장)
        String thumbnailFileName = originalImage.getName().replace(".", "_thumb.");
        File thumbnailFile = new File(uploadDir, thumbnailFileName);

        // 썸네일 생성
        Thumbnails.of(originalImage)
                .size(100, 100)  // 썸네일 크기 지정
                .toFile(thumbnailFile);  // 썸네일 파일로 저장

        return thumbnailFile;
    }
}