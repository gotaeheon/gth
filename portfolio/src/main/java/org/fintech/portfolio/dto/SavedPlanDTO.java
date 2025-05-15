package org.fintech.portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedPlanDTO {
	
	private Long saveId; //마이페이지 저장때 쓸 번호
    private Long planId; //게시물번호
    private String departureRegionName; //출발지역
    private String arrivalRegionName; //도착지역
    private String hotelName; // 호텔이름
    private int totalPrice; // 총가격
    private LocalDateTime savedAt; // 저장 시간 필드 추가
    

}
