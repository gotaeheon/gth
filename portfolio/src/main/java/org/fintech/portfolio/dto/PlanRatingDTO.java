package org.fintech.portfolio.dto;

import org.fintech.portfolio.entity.Plan;

import lombok.Getter;

@Getter
public class PlanRatingDTO {
    private Plan plan;		//리스트
    private Double avgRating;//평점
    private int totalPrice;  // 가격 

    public PlanRatingDTO(Plan plan, Double avgRating, int totalPrice) {
        this.plan = plan;
        this.avgRating = avgRating;
        this.totalPrice = totalPrice;
    }

}
