package org.fintech.portfolio.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.fintech.portfolio.dto.MemberDTO;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude="roleSet")
public class MemberEntity {
	
    @Id
    @Column(name = "mb_id") //  <- 쓴 이유: JPA에서 언더바가 인식을 못해서 네이밍룰을 못사용 네이밍룰을 사용하기 위해서 했슴
    private String mbId; // 회원 아이디

    @NotNull
    @Column(name = "mb_pw")
    private String mbPw; // 회원 비밀번호

    @Column(name = "mb_name") //회원 이름
    private String mbName;

    @Column(name = "mb_nick") //회원 닉네임
    private String mbNick;

    @Column(name = "mb_email") //회원 이메일 
    private String mbEmail;

    @Column(name = "mb_gender") //회원 성별 
    private String mbGender;

    @Column(name = "mb_hp") //회원 연락처 
    private String mbHp;

    @CreationTimestamp //INSERT 시 자동으로 값을 채워줌
    @Column(name = "mb_sysdate")  //등록일자 
    private LocalDateTime mbSysdate;

    @Column(name = "mb_social") //소셜 로그인 
    private Boolean mbSocial = false;
    
    @Column(name = "mb_deleted") //탈퇴여부
    private Boolean mbDeleted = false;

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    @Column(name = "mb_update") //수정일 
    private LocalDateTime mbUpdate;
   
    //DTO를 Entity로 변환하는 정적 메서드
    public static MemberEntity toEntity(MemberDTO dto) {
        return MemberEntity.builder()
                .mbId(dto.getMbId())
                .mbPw(dto.getMbPw())
                .mbName(dto.getMbName())
                .mbNick(dto.getMbNick())
                .mbEmail(dto.getMbEmail())
                .mbGender(dto.getMbGender())
                .mbHp(dto.getMbHp())
                .mbSocial(dto.isMbSocial())
                .build();
    }
    

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "mb_id"))
    @Enumerated(EnumType.STRING)  // Enum 값을 문자열로 저장
    @Column(name = "role")
    private Set<MemberRole> roleSet = new HashSet<>();
  	
  	
  	public void changembPw(String mbPw) {
		this.mbPw = mbPw;
	}
	
	public void changembName(String mbName) {
		this.mbName = mbName;
	}
	
	public void addRole(MemberRole memberRole) {
	    if (this.roleSet == null) {
	        this.roleSet = new HashSet<>();
	    }
	    this.roleSet.add(memberRole);
	}
	
	public void clearRoles() {
		this.roleSet.clear();
	}
	
}
