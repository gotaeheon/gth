package org.fintech.portfolio.dto;

import org.fintech.portfolio.entity.Plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
	
	// 계획 번호
    private Long planId;

    // 출발/도착 지역
    private String departureRegionName;
    private String arrivalRegionName;

    // 출발 교통편
    private String departureTransportType;
    private String departureCompany;
    private String departureTime;
    private String departureArrivalTime;
    private int departurePrice;
    private String departureTransportInfo;
    			

    // 숙소 정보
    private String hotelName;
    private int hotelPrice;
    private String hotelAddress;
    private float hotelRating;

    // 복귀 교통편
    private String returnTransportType;
    private String returnCompany;
    private String returnTime;
    private String returnArrivalTime;
    private int returnPrice;
    private String returnTransportInfo;

    // 총 비용
    private int totalPrice;

    // 상세 페이지 링크
    private String detailUrl;

    // Entity -> DTO 변환
    public static PlanDTO fromEntity(Plan entity) {
        return PlanDTO.builder()
                .planId(entity.getPlanId())
                .departureRegionName(entity.getDepartureRegionName())
                .arrivalRegionName(entity.getArrivalRegionName())
                .departureTransportType(entity.getDepartureTransportType())
                .departureCompany(entity.getDepartureCompany())
                .departureTime(entity.getDepartureTime().toString())
                .departureArrivalTime(entity.getDepartureArrivalTime().toString())
                .departurePrice(entity.getDeparturePrice())
                .departureTransportInfo(
                    entity.getDepartureCompany() + " " +
                    entity.getDepartureTransportType() + " " +
                    " 출발 "+entity.getDepartureTime().toString() + " → " +
                    entity.getDepartureArrivalTime().toString() + " 도착"
                )
                .hotelName(entity.getHotelName())
                .hotelAddress(entity.getHotelAddress())
                .hotelRating(entity.getHotelRating())
                .hotelPrice(entity.getHotelPrice())
                .returnTransportType(entity.getReturnTransportType())
                .returnCompany(entity.getReturnCompany())
                .returnTime(entity.getReturnTime().toString())
                .returnArrivalTime(entity.getReturnArrivalTime().toString())
                .returnPrice(entity.getReturnPrice())
                .returnTransportInfo(
                    entity.getReturnCompany() + " " +
                    entity.getReturnTransportType() + " " +
                    entity.getReturnTime().toString() + " 출발 → " +
                    entity.getReturnArrivalTime().toString() + " 도착"
                )
                .totalPrice(entity.getTotalPrice())
                .detailUrl(entity.getDetailUrl())
                .build();
    }
}
