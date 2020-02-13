package com.miro.widgetapi.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.model.RectangleFilter;

public class WidgetStoreUtils {

	public static Page<WidgetEntity> getPage(Pageable pageable, Collection<WidgetEntity> filteredEntities) {
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        
        List<WidgetEntity> content = new ArrayList<WidgetEntity>();
        if (startItem < filteredEntities.size()) {
        	List<WidgetEntity> entities = new ArrayList<WidgetEntity>(filteredEntities);
            int toIndex = Math.min(startItem + pageSize, filteredEntities.size());
            entities.subList(startItem, toIndex).stream().forEach(w -> content.add(new WidgetEntity(w)));
        }
 
		return new PageImpl<WidgetEntity>(content, pageable, filteredEntities.size());
	}
	
	public static boolean isWidgetInsideRectangle(WidgetEntity widget, RectangleFilter filter) {
		boolean insideXPlane = filter.getMinX() <= widget.getX() && filter.getMaxX() >= widget.getX() + widget.getWidth();
		boolean insideYPlane = filter.getMinY() <= widget.getY() && filter.getMaxY() >= widget.getY() + widget.getHeight();
		return insideXPlane && insideYPlane;
	}

}
