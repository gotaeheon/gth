package org.fintech.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId; // 댓글 게시물 번호

    @Column(name = "bno", nullable = false)
    private Long bno; // 게시물 번호

    @Column(name = "mb_id", nullable = false, length = 10)
    private String mbId; // 회원 아이디

    @Column(name = "content", nullable = false, length = 1000)
    private String content; // 내용

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성일 

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정일

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
