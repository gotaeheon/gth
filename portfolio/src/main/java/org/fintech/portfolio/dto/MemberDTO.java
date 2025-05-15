package org.fintech.portfolio.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MemberDTO {

    @NotBlank(message = "아이디는 필수 입력입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 10자 이하로 입력해주세요.")
    private String mbId;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, max = 12, message = "비밀번호는 8자 이상 12자 이하로 입력해주세요.")
    private String mbPw;

    @Transient
    private String mbPwRe;

    @NotBlank(message = "이름은 필수 입력입니다.")
    @Size(min = 2, max = 30, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
    private String mbName;

    @NotBlank(message = "닉네임은 필수 입력입니다.")
    @Size(min = 2, max = 30, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String mbNick;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String mbEmail;

    @NotBlank(message = "성별을 선택해주세요.")
    @Pattern(regexp = "^(M|F)$", message = "성별은 M(남성) 또는 F(여성) 중 하나여야 합니다.")
    private String mbGender;

    @NotBlank(message = "휴대폰 번호는 필수 입력입니다.")
    @Pattern(regexp = "^01[016789]\\d{3,4}\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. 예: 01012345678")
    private String mbHp;

    private boolean mbSocial;

    private LocalDateTime mbSysdate;
    private LocalDateTime mbUpdate;

    //  탈퇴 여부 (0: 정상, 1: 탈퇴)
    private int mbDel;
}
