package org.fintech.portfolio.entity;

import java.util.List;

import groovy.transform.EqualsAndHashCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "reviews") // 리뷰 목록을 제외하고 출력하도록 설정
@Entity
@Table(name = "plan") // 'plan' 테이블과 매핑되는 엔티티 클래스
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값은 자동 생성되는 값
    @Column(name = "plan_id") 
    private Long planId;  // 일정 번호 (Primary Key)

    @Column(name = "departure_region_name") 
    private String departureRegionName; // 출발 지역

    @Column(name = "arrival_region_name") 
    private String arrivalRegionName; // 도착 지역

    @Column(name = "departure_transport_type") 
    private String departureTransportType; // 출발 교통수단 종류

    @Column(name = "departure_company") 
    private String departureCompany; // 출발 교통수단 회사 

    @Column(name = "departure_time") 
    private String departureTime; // 출발 시간 

    @Column(name = "departure_arrival_time") 
    private String departureArrivalTime; // 도착 시간 

    @Column(name = "departure_price") 
    private Integer departurePrice; // 출발 교통수단 가격

    @Column(name = "hotel_name") 
    private String hotelName; // 숙소 이름

    @Column(name = "hotel_address") 
    private String hotelAddress; // 숙소 주소 

    @Column(name = "hotel_rating") 
    private Float hotelRating; // 숙소 평점

    @Column(name = "hotel_price") 
    private Integer hotelPrice; // 숙소 가격 

    @Column(name = "return_transport_type") 
    private String returnTransportType; // 복귀 교통수단 종류 

    @Column(name = "return_company") 
    private String returnCompany; // 복귀 교통수단 회사 

    @Column(name = "return_time") 
    private String returnTime; // 복귀 시간

    @Column(name = "return_arrival_time") 
    private String returnArrivalTime; // 복귀 도착 시간 

    @Column(name = "return_price") 
    private Integer returnPrice; // 복귀 교통수단 가격 

    @Column(name = "total_price") 
    private Integer totalPrice; // 전체 일정 가격 

    @Column(name = "detail_url") 
    private String detailUrl; // 일정 상세 URL 

    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
    private List<Review> reviews;  // 하나의 Plan에는 여러 개의 Review가 있을 수 있음. Plan에 대한 리뷰 목록
}
