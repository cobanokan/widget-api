package com.miro.widgetapi.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.model.RectangleFilter;

@ConditionalOnMissingBean(value = H2WidgetStore.class)
@Component
public class InMemoryWidgetStore implements WidgetStore {
	private static final int DEFAULT_Z = 0;
	
	private SortedSet<WidgetEntity> sortedWidgets = new TreeSet<WidgetEntity>();
	
	@Override
	public WidgetEntity save(WidgetEntity entity) {
		WidgetEntity widget = new WidgetEntity(entity);	
		sortedWidgets.add(widget);
		return new WidgetEntity(widget);
	}
	
	@Override
	public Optional<WidgetEntity> getWidget(String id) {
		return sortedWidgets.stream().filter(w -> w.getId().equals(id)).findFirst().map(w -> w = new WidgetEntity(w));
	}
	
	@Override
	public Page<WidgetEntity> getWidgetsSortedByZ(Pageable pageable) {
		return getPage(pageable, sortedWidgets);
	}
	
	@Override
	public Page<WidgetEntity> getWidgetsSortedByZAndFiltered(RectangleFilter filter, Pageable pageable) {
    	List<WidgetEntity> filteredEntities =  sortedWidgets.stream().filter(w ->  isWidgetInsideRectangle(w, filter)).collect(Collectors.toList());
    	return  getPage(pageable, filteredEntities);
	}

	@Override
	public void removeWidget(String id) {
		sortedWidgets.removeIf(w -> w.getId().equals(id));
	}
	
	@Override
	public Integer getNextZ() {
		return sortedWidgets.isEmpty() ? DEFAULT_Z : sortedWidgets.last().getZ() + 1;
	}

	@Override
	public void shiftWidgetsAboveZUpwards(Integer z) {
		sortedWidgets.stream().filter(w -> w.getZ() >=z).forEach(w -> {
			w.setZ(w.getZ() + 1);
			w.setLastModification(new Date());
		});
	}

	@Override
	public WidgetEntity update(WidgetEntity entity) {
		removeWidget(entity.getId());
		return save(entity);
	}
	
	public static Page<WidgetEntity> getPage(Pageable pageable, Collection<WidgetEntity> entities) {
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        
        List<WidgetEntity> content = new ArrayList<WidgetEntity>();
        if (startItem < entities.size()) {
        	List<WidgetEntity> copy = new ArrayList<WidgetEntity>(entities);
            int toIndex = Math.min(startItem + pageSize, copy.size());
            copy.subList(startItem, toIndex).stream().forEach(w -> content.add(new WidgetEntity(w)));
        }
 
		return new PageImpl<WidgetEntity>(content, pageable, entities.size());
	}
	
	public static boolean isWidgetInsideRectangle(WidgetEntity widget, RectangleFilter filter) {
		boolean insideXPlane = filter.getMinX() <= widget.getX() && filter.getMaxX() >= widget.getX() + widget.getWidth();
		boolean insideYPlane = filter.getMinY() <= widget.getY() && filter.getMaxY() >= widget.getY() + widget.getHeight();
		return insideXPlane && insideYPlane;
	}
}
