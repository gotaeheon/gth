package org.fintech.portfolio.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saved_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedPlan { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_id") 
    private Long saveId; // 일정 저장번호

    @Column(name = "mb_id") // 맴버아이디
    private String memberId; 

    @Column(name = "plan_id") // 추천일정 아이디
    private Long planId;

    @Column(name = "saved_at") // 저장일정
    private LocalDateTime savedAt;

    @Column(name = "departure_region_name") // 출발지 이름 추가
    private String departureRegionName;  
    
    @Column(name = "arrival_region_name") // 도착지 이름 추가
    private String arrivalRegionName;  
    
    
}
