package com.boulifa.widget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.boulifa.widget.entity.Widget;

@Repository
public interface WidgetRepository extends PagingAndSortingRepository<Widget, Long> {
	int countById(Long id);
	
	@Query("SELECT max(w.depthZ) FROM Widget w")
	int getMaxDepthZ();
	
	// If CenterX, CenterY, Width,Height and DepthZ are matching we consider the widgets as equal 
	Widget findByCenterXAndCenterYAndWidthAndHeightAndDepthZ(int centerX, int centerY, int width, int height, int depthZ);
	
	List<Widget> findByDepthZGreaterThanEqualOrderByDepthZAsc(int depthZ);
	
	boolean existsWidgetByDepthZ(int depthZ);
}
