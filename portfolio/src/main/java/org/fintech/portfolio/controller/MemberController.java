package org.fintech.portfolio.controller;

import org.fintech.portfolio.dto.MemberDTO;
import org.fintech.portfolio.entity.MemberEntity;
import org.fintech.portfolio.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginGet() {
        return "member/login";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute("memberDTO") MemberDTO memberDTO,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        log.info("회원가입 요청: {}", memberDTO);

        if (!memberDTO.getMbPw().equals(memberDTO.getMbPwRe())) {
            bindingResult.rejectValue("mbPw", "error.mbPwRe", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            MemberEntity result = memberService.join(memberDTO);
            redirectAttributes.addFlashAttribute("result", result);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }

        return "redirect:/member/joinProcess";
    }

    // 아이디 중복 확인
    @GetMapping("/check-id")
    public ResponseEntity<String> checkId(@RequestParam("id") String id) {
        boolean isDuplicate = memberService.isIdDuplicate(id);
        return ResponseEntity.ok(isDuplicate ? "duplicate" : "ok");
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam("email") String email) {
        boolean isDuplicate = memberService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate ? "duplicate" : "ok");
    }

    // 회원가입 완료 페이지
    @GetMapping("/joinProcess")
    public String joinProcess() {
        return "member/joinProcess";
    }

    // 내 정보 보기 (로그인 필요)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/memberInfo")
    public String memberInfo(Model model, @AuthenticationPrincipal User user) {
        MemberEntity member = memberService.findByMbId(user.getUsername());
        model.addAttribute("result", member);
        return "member/memberInfo";
    }

    // 수정 화면
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/memberModify")
    public String modifyForm(Model model, @AuthenticationPrincipal User user) {
        MemberEntity member = memberService.findByMbId(user.getUsername());

        MemberDTO dto = new MemberDTO();
        dto.setMbId(member.getMbId());
        dto.setMbName(member.getMbName());
        dto.setMbNick(member.getMbNick());
        dto.setMbGender(member.getMbGender());
        dto.setMbHp(member.getMbHp());
        dto.setMbEmail(member.getMbEmail());

        model.addAttribute("memberDTO", dto);
        return "member/memberModify";
    }

    // 수정 완료
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/memberModify")
    public String modify(@ModelAttribute MemberDTO memberDTO,
                         @AuthenticationPrincipal User user,
                         RedirectAttributes rttr) {
        memberService.modify(memberDTO);
        rttr.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
        return "redirect:/member/memberInfo";
    }

    // 회원 탈퇴
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal User user,
                           RedirectAttributes redirectAttributes) {
        memberService.withdraw(user.getUsername());
        redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");
        return "redirect:/home";
    }
}
