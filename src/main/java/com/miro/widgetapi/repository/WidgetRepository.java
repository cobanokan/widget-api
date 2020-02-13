package com.miro.widgetapi.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.model.RectangleFilter;

@ConditionalOnProperty(name = "h2.enabled", havingValue = "true")
public interface WidgetRepository extends PagingAndSortingRepository<WidgetEntity, String>{
 
	Page<WidgetEntity> findAllByOrderByZAsc(Pageable pageable);
		
	@Query("select w from widget AS w where w.x >= :#{#filter.minX} and w.x + w.width <= :#{#filter.maxX} and w.y <= :#{#filter.minY} and w.y + w.height <= :#{#filter.maxY} order by w.z")
	Page<WidgetEntity> findFilteredAndOrdered(@Param("filter") RectangleFilter filter, Pageable pageable);
	
	Optional<WidgetEntity> findFirstByOrderByZDesc();
	
	@Modifying
	@Query("update widget w set w.z = w.z + 1, last_modification = ?2 where w.z >= ?1")
	void shiftWidgetsAboveZUpdwards(Integer z, Date date);
}
