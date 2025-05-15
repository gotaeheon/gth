package org.fintech.portfolio.controller;

import java.util.List;

import org.fintech.portfolio.dto.BoardDTO;
import org.fintech.portfolio.entity.BoardComment;
import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.entity.BoardImage;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.service.BoardCommentService;
import org.fintech.portfolio.service.BoardImageService;
import org.fintech.portfolio.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardCommentService commentService;
    private final BoardImageService boardImageService;

    @Autowired
    public BoardController(BoardService boardService, BoardCommentService commentService, BoardImageService boardImageService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.boardImageService = boardImageService;
    }

    // 게시물 목록 페이지
    @GetMapping("/list")
    public String getBoardList(Model model,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "searchType", required = false) String searchType,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        int size = 10;
        Page<BoardEntity> boardPage = boardService.searchBoards(page, size, searchType, keyword); // 페이징,검색조건 포함된 리스트

        for (BoardEntity board : boardPage.getContent()) {
            List<BoardImage> images = boardImageService.getImagesByBoard(board.getBno());
            if (!images.isEmpty()) {
                board.setThumbnailFileName(images.get(0).getUuid() + "_" + images.get(0).getFileName());
            }
        }

        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("totalPages", boardPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "board/list";
    }

    // 게시물 상세 페이지 조회 및 조회수 증가
    @PreAuthorize("isAuthenticated()")  // 접근 권한: 인증된 사용자
    @GetMapping("/{bno}")
    public String getBoard(@PathVariable("bno") Long bno, @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        BoardEntity board = boardService.viewBoard(bno); // 게시글 상세
        List<BoardImage> images = boardImageService.getImagesByBoard(bno); // 이미지
        List<BoardComment> comments = commentService.getCommentsByBno(bno); // 댓글 목록

        model.addAttribute("board", board);
        model.addAttribute("images", images);
        model.addAttribute("comments", comments);
        model.addAttribute("page", page);

        return "board/read"; // 상세 페이지 뷰
    }

    // 게시물 좋아요 증가
    @PostMapping("/{bno}/like")
    public String likeBoard(@PathVariable("bno") Long bno) {
        boardService.likeBoard(bno);
        return "redirect:/board/" + bno;
    }

    // 게시물 삭제
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 삭제 가능
    @PostMapping("/{bno}/delete")
    public String deleteBoard(@PathVariable("bno") Long bno) {
        boardService.deleteBoard(bno);
        return "redirect:/board/list";
    }

    // 게시물 생성 페이지
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 접근 가능
    @GetMapping("/register")
    public String newBoardForm(Model model) {
        model.addAttribute("board", new BoardEntity());
        return "board/register";
    }

    // 게시물 등록 처리
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 게시물 등록 가능
    @PostMapping("/register")
    public String createBoard(@ModelAttribute("board") BoardEntity boardEntity,
                              @RequestParam("imageFiles") List<MultipartFile> images) {

        // 게시물 작성자 설정
        // Authentication 객체에서 로그인한 사용자 정보를 가져오기
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        boardEntity.setWriter(username);

        // 게시물 저장
        boardService.createBoard(boardEntity);

        // 이미지 여러 개 업로드 처리
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                boardImageService.uploadImage(image, boardEntity.getBno());
            }
        }

        return "redirect:/board/list"; // 목록 페이지로 리다이렉트
    }

    // 게시글 수정 폼 이동
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 게시물 수정 가능
    @GetMapping("/{bno}/edit")
    public String editForm(@PathVariable("bno") Long bno, @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        BoardDTO boardDTO = boardService.findById(bno);

        // 게시물 작성자와 로그인한 사용자가 동일한지 확인
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals(boardDTO.getWriter())) {
            return "redirect:/board/" + bno;
        }

        model.addAttribute("page", page);
        model.addAttribute("board", boardDTO);

        // 게시물의 기존 이미지를 모델에 추가
        List<BoardImage> images = boardImageService.getImagesByBoard(bno);
        model.addAttribute("images", images);

        return "board/edit";
    }

    // 게시물 수정 처리
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 게시물 수정 가능
    @PostMapping("/{bno}/edit")
    public String editBoard(@PathVariable("bno") Long bno, 
                            @ModelAttribute BoardDTO boardDTO, 
                            @RequestParam(value = "image", required = false) MultipartFile image,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds) {

        boardService.update(bno, boardDTO);

        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long deleteImageId : deleteImageIds) {
                boardImageService.deleteImage(bno, deleteImageId);
            }
        }

        if (image != null && !image.isEmpty()) {
            boardImageService.uploadImage(image, bno);
        }

        // 수정 후 상세보기 페이지로 이동 + 원래 페이지 정보 유지
        return "redirect:/board/" + bno + "?page=" + page;
    }

    // 댓글 추가
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 댓글을 추가할 수 있음
    @PostMapping("/{bno}/comment")
    public String addComment(@PathVariable("bno") Long bno,
                             @RequestParam("content") String content) {

        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();

        BoardComment comment = new BoardComment();
        comment.setBno(bno);
        comment.setMbId(username);  // 로그인한 사용자 ID로 댓글 작성
        comment.setContent(content);

        commentService.saveComment(comment); // 댓글 저장

        return "redirect:/board/" + bno; // 댓글 저장 후 다시 게시물 상세 페이지로 리다이렉트
    }

    // 댓글 삭제
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 댓글 삭제 가능
    @PostMapping("/{bno}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("bno") Long bno,
                                @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId); // 댓글 삭제
        return "redirect:/board/" + bno; // 삭제 후 게시물 상세 페이지로 리다이렉트
    }

    // 댓글 수정
    @PreAuthorize("hasRole('USER')")  // ROLE_USER 권한을 가진 사용자만 댓글 수정 가능
    @PostMapping("/{bno}/comment/{commentId}/edit")
    public String updateComment(@PathVariable("bno") Long bno,
                                @PathVariable("commentId") Long commentId,
                                @RequestParam("newContent") String newContent) {

        commentService.updateComment(commentId, newContent); // 댓글 수정
        return "redirect:/board/" + bno; // 수정된 댓글을 다시 게시물 상세 페이지로 리다이렉트
    }
}
