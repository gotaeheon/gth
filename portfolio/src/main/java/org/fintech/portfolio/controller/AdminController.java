package org.fintech.portfolio.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.fintech.portfolio.dto.PlanRatingDTO;
import org.fintech.portfolio.entity.BoardEntity;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.entity.Plan;
import org.fintech.portfolio.service.AdminService;
import org.fintech.portfolio.service.MemberService;
import org.fintech.portfolio.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final PlanService planService;

    //전체 회원 목록 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/adminMemberList")
    public String listAllMembers(Model model,
                                  @RequestParam(value = "msg", required = false) String msg,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "searchType", required = false) String searchType,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        int size = 5;  // 한 페이지에 5개의 게시글을 표시
        Page<MemberEntity> boardPage = adminService.getMemberList(page, size, searchType, keyword); // 페이지네이션 처리된 회원 목록

        // 페이지네이션된 회원 목록과 관련된 데이터
        model.addAttribute("boards", boardPage.getContent());  // 최신순으로 정렬된 회원목록 리스트
        model.addAttribute("totalPages", boardPage.getTotalPages());  // 전체 페이지 수
        model.addAttribute("currentPage", page);  // 현재 페이지
        model.addAttribute("searchType", searchType);  // 검색 조건
        model.addAttribute("keyword", keyword);  // 검색어

        // 검색 메시지 처리
        if (msg != null && !msg.isEmpty()) {
            model.addAttribute("msg", msg);
        }

        return "admin/adminMemberList";
    }

    // 회원 상세보기 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/{mbId}")
    public String viewMember(@PathVariable("mbId") String mbId,
    						@RequestParam(value = "page", defaultValue = "1") int page,
    						Model model) {
    	

        MemberEntity member = memberService.findByMbId(mbId);
        

        model.addAttribute("member", member);
        model.addAttribute("page", page);
        
        return "admin/adminMemberInfo";
    }

    // 회원 복구 처리
    @PostMapping("/member/restore")
    @PreAuthorize("isAuthenticated()")
    public String restoreMember(@RequestParam("mbId") String mbId, RedirectAttributes rttr) {
        memberService.restore(mbId);
        rttr.addFlashAttribute("msg", "회원 복구가 완료되었습니다.");
        return "redirect:/admin/adminMemberList";
    }

    // 리뷰 및 일정 목록 페이지 (리스트형 보기)
    @GetMapping("/review-plans")
    public String reviewPlansPage(Model model,
    							 @RequestParam(value = "page", defaultValue = "1") int page,
    							 @RequestParam(value = "searchType", required = false) String searchType,
    							 @RequestParam(value = "keyword", required = false) String keyword) {
    	
    	int size = 10;
        // 페이징된 일정 목록 가져오기
        Page<Plan> planPage = adminService.findPlansWithPaging(page, size, searchType, keyword);

        List<Map<String, Object>> planWithRatingList = new ArrayList<>();

        // 일정 목록과 평균 평점 처리
        for (Plan plan : planPage.getContent()) {
            Double avgRating = adminService.getAverageRatingByPlanId(plan.getPlanId());

            Map<String, Object> entry = new HashMap<>();
            
            entry.put("plan", plan);
            entry.put("avgRating", avgRating != null ? String.format("%.1f", avgRating) : "평점 없음");

            planWithRatingList.add(entry);
        }

        // 모델에 페이징 데이터와 함께 추가
        model.addAttribute("plans", planWithRatingList);
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);  // 검색 조건
        model.addAttribute("keyword", keyword);  // 검색어
        model.addAttribute("totalPages", planPage.getTotalPages());
        model.addAttribute("totalItems", planPage.getTotalElements());

        return "admin/reviewPlans";
    }

    // 리뷰 및 일정 차트 페이지    
    @GetMapping("/review-plans/chart")
    @PreAuthorize("isAuthenticated()")
    public String reviewPlansChart(@RequestParam(name = "sort", defaultValue = "ratingDesc") String sort, Model model) {
        List<PlanRatingDTO> sortedPlans = planService.getSortedPlans(sort);
        model.addAttribute("plans", sortedPlans);
        model.addAttribute("sort", sort);
        return "admin/reviewPlansChart";
    }

    // 차트용 데이터 JSON 응답 (AJAX 호출용)
    @GetMapping("/review-plans/data")
    @ResponseBody
    public List<Map<String, Object>> getChartData(@RequestParam(name = "sort", defaultValue = "ratingDesc") String sort) {
        List<PlanRatingDTO> list = planService.getSortedPlans(sort);

        return list.stream().map(dto -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", dto.getPlan().getDepartureRegionName() 
                            + " - " + dto.getPlan().getHotelName()
                            + " - " + dto.getPlan().getArrivalRegionName());
            map.put("value", sort.startsWith("rating") ? dto.getAvgRating() : dto.getTotalPrice());
            return map;
        }).collect(Collectors.toList());
    }

    // 추천 일정 등록 페이지
    @GetMapping("/registerPlan")
    @PreAuthorize("isAuthenticated()")
    public String registerPlanPage() {
        return "admin/registerPlan";
    }
    
    @PostMapping("/registerPlan")
    public String registerPlan(@ModelAttribute Plan plan) {
        // 받은 plan 데이터를 서비스로 전달하여 DB에 저장
        planService.savePlan(plan);
        
        return "redirect:/home";  // 등록 후 대시보드로 리다이렉트
    }
}
