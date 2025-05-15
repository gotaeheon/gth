package org.fintech.portfolio.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.fintech.portfolio.dto.MemberSecurityDTO;
import org.fintech.portfolio.dto.PlanDTO;
import org.fintech.portfolio.dto.ReviewDTO;
import org.fintech.portfolio.dto.SavedPlanDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.entity.Review;
import org.fintech.portfolio.entity.ReviewImage;
import org.fintech.portfolio.entity.SavedPlan;
import org.fintech.portfolio.service.PlanService;
import org.fintech.portfolio.service.ReviewImageService;
import org.fintech.portfolio.service.ReviewService;
import org.fintech.portfolio.service.SavedPlanService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {

    private final SavedPlanService savedPlanService;
    private final PlanService planService;
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @PostMapping("/search")
    public String searchPlans(@RequestParam("departureRegionId") int departureRegionId,
                              @RequestParam("budget") int budget,
                              Model model) {
        List<PlanDTO> plans = planService.findPlansByRegionAndBudget(departureRegionId, budget);
        model.addAttribute("plans", plans);
        return "plan/searchInfo";
    }

    @GetMapping({"/detail/{planId}", "/mypage/detail/{planId}"})
    public String viewPlanDetail(@PathVariable("planId") Long planId,
    							Model model) {
    	
        PlanDTO planDTO = PlanDTO.fromEntity(planService.findById(planId));
        
        model.addAttribute("plan", planDTO);
        
        return "plan/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/save")
    public String savePlan(@RequestParam("planId") Long planId,
                           @AuthenticationPrincipal MemberSecurityDTO userDetails,
                           RedirectAttributes redirectAttributes) {

        String mbId = userDetails.getMbId(); 

        try {
            savedPlanService.savePlanForMember(mbId, planId);
            redirectAttributes.addFlashAttribute("success", "일정이 마이페이지에 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "일정 저장에 실패했습니다.");
        }

        return "redirect:/plans/detail/" + planId + "?saved=true";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal MemberSecurityDTO userDetails,
    					Model model) {
    	
    	if (userDetails == null) {
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }
    	
        List<SavedPlanDTO> savedPlans = savedPlanService.getSavedPlanDTOsByMemberId(userDetails.getMbId());
        
        savedPlans.sort(Comparator.comparing(SavedPlanDTO::getSavedAt).reversed());
        model.addAttribute("savedPlans", savedPlans);
        return "member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{saveId}")
    public String deletePlan(@PathVariable("saveId") Long saveId,
                             RedirectAttributes redirectAttributes) {
        Optional<SavedPlan> savedPlanOpt = savedPlanService.findById(saveId);
        if (savedPlanOpt.isPresent()) {
            savedPlanService.deleteSavedPlan(saveId);
            redirectAttributes.addFlashAttribute("success", "일정이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "일정을 찾을 수 없습니다.");
        }
        return "redirect:/plans/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{planId}")
    public String writeReview(@PathVariable("planId") Long planId,
    						 @AuthenticationPrincipal MemberSecurityDTO userDetails,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (reviewService.hasReviewedPlan(planId, userDetails.getMbId())) {
            redirectAttributes.addFlashAttribute("error", "이미 리뷰를 작성한 일정입니다.");
            return "redirect:/plans/mypage";
        }

        model.addAttribute("planId", planId);
        return "plan/review";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/saveReview")
    public String saveReview(@ModelAttribute ReviewDTO reviewDTO,
                             @RequestParam("imageFile") MultipartFile[] imageFiles,
                             @AuthenticationPrincipal MemberSecurityDTO userDetails,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        Plan plan = planService.findById(reviewDTO.getPlanId());
        
        if (plan == null) {
            redirectAttributes.addFlashAttribute("error", "일정 정보를 찾을 수 없습니다.");
            return "redirect:/plans/mypage";
        }

        String prefixedTitle = "[" + plan.getDepartureRegionName() + "→" + plan.getArrivalRegionName() + "] " + reviewDTO.getReTitle();

        Review review = new Review();
        review.setMbId(userDetails.getMbId());
        review.setReTitle(prefixedTitle);
        review.setReComment(reviewDTO.getReComment());
        review.setReScore(reviewDTO.getReScore());
        review.setPlan(plan);

        Review savedReview = reviewService.saveReview(reviewDTO.getPlanId(), review);

        for (MultipartFile image : imageFiles) {
            if (!image.isEmpty()) {
                reviewImageService.uploadImage(image, savedReview.getReNo());
            }
        }

        return "redirect:/plans/mypage";
    }

    @GetMapping("/list/{planId}")
    public String listReviews(@PathVariable("planId") Long planId, Model model) {
        model.addAttribute("reviews", reviewService.getReviewsByPlanId(planId));
        model.addAttribute("planId", planId);
        return "plan/reviewList";
    }

    @GetMapping("/reviews")
    public String getReviewList(Model model,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "searchType", required = false) String searchType,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "sort", required = false) String sort) {

        int size = 5;
        Page<Review> reviewPage = reviewService.getReviewList(page, size, searchType, keyword, sort);

        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);

        return "plan/allReviews";
    }

    @GetMapping("/reviews/{rno}")
    public String getReview(@PathVariable("rno") Long rno,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            Model model,
                            @AuthenticationPrincipal MemberEntity loginUser) {

        List<ReviewImage> images = reviewImageService.getImagesByReview(rno);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("images", images);
        model.addAttribute("review", reviewService.viewReview(rno));
        model.addAttribute("page", page);
        return "plan/reviewRead";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/{rno}/like")
    public String likeReview(@PathVariable("rno") Long rno) {
        reviewService.likeReview(rno);
        return "redirect:/plans/reviews/" + rno;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/{rno}/delete")
    public String deleteReview(@PathVariable("rno") Long rno,
                               RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(rno);
            redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "리뷰 삭제에 실패했습니다.");
        }
        return "redirect:/plans/reviews";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reviews/{rno}/edit")
    public String editForm(@PathVariable("rno") Long rno,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           Model model,
                           @AuthenticationPrincipal MemberEntity loginUser) {

        ReviewDTO review = reviewService.getReview(rno);
        List<ReviewImage> images = reviewImageService.getImagesByReview(rno);

        model.addAttribute("review", review);
        model.addAttribute("images", images);
        model.addAttribute("page", page);

        return "plan/reviewEdit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/{rno}/edit")
    public String editReview(@PathVariable("rno") Long rno,
                             @ModelAttribute ReviewDTO reviewDTO,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds) {

        reviewService.update(rno, reviewDTO);

        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long deleteImageId : deleteImageIds) {
                reviewImageService.deleteImage(rno, deleteImageId);
            }
        }

        if (image != null && !image.isEmpty()) {
            reviewImageService.uploadImage(image, rno);
        }

        return "redirect:/plans/reviews/" + rno + "?page=" + page;
    }
}
