package org.fintech.portfolio.controller;

import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.entity.BoardImage;
import org.fintech.portfolio.entity.Review;
import org.fintech.portfolio.entity.ReviewImage;
import org.fintech.portfolio.service.BoardImageService;
import org.fintech.portfolio.service.BoardService;
import org.fintech.portfolio.service.ReviewImageService;
import org.fintech.portfolio.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final BoardService boardService;
    private final ReviewService reviewService;
    private final BoardImageService boardImageService;
    private final ReviewImageService reviewImageService;
    
    public MainController(BoardService boardService, ReviewService reviewService, BoardImageService boardImageService, ReviewImageService reviewImageService ) {
        this.boardService = boardService;
        this.reviewService = reviewService;
        this.boardImageService = boardImageService;
        this.reviewImageService = reviewImageService;
    }

    // 메인 페이지 진입 시 인기 게시물과 평점 높은 리뷰를 함께 넘겨줌
    @GetMapping("/home")
    public String showMain(Model model) {

        // 인기 게시물 가져오기
        List<BoardEntity> popularBoards = boardService.getTopLikedBoards();
        List<BoardEntity> boards = boardService.getAllBoards();

        // 인기 게시물에 썸네일 설정
        for (BoardEntity board : popularBoards) {
            List<BoardImage> images = boardImageService.getImagesByBoard(board.getBno());
            
            if (!images.isEmpty()) {
            	
                board.setThumbnailFileName(images.get(0).getUuid() + "_" + images.get(0).getFileName());
            }
        }

        // 평점 높은 순으로 인기 리뷰 가져오기
        List<Review> topRatedReviews = reviewService.getReviewsSortedByScore();
        List<Review> reviews = reviewService.getAllReviews();
        
        for (Review review : topRatedReviews) {
            List<ReviewImage> images = reviewImageService.getImagesByReview(review.getReNo());
            
            if (!images.isEmpty()) {
            	
            	review.setThumbnailFileName(images.get(0).getUuid() + "_" + images.get(0).getFileName());
            }
        }

        System.out.println("인기 게시물 수: " + popularBoards.size());
        System.out.println("전체 게시물 수: " + boards.size());
        System.out.println("인기 리뷰 수: " + topRatedReviews.size());

        // 모델에 데이터 추가
        model.addAttribute("popularBoards", popularBoards);
        model.addAttribute("topRatedReviews", topRatedReviews);
        model.addAttribute("reviews", reviews);
        model.addAttribute("boards", boards);

        return "index";  // 메인 페이지 뷰
    }

}
