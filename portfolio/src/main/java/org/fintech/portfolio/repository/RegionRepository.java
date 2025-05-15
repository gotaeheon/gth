package org.fintech.portfolio.repository;

import org.fintech.portfolio.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
	
	//지역검색
    RegionEntity findByRegionName(String regionName);
}
