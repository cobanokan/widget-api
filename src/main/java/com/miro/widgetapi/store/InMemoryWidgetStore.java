package com.miro.widgetapi.store;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.Page;
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
	public Page<WidgetEntity> getSortedWidgets(Pageable pageable) {
		return WidgetStoreUtils.getPage(pageable, sortedWidgets);
	}
	
	@Override
	public Page<WidgetEntity> getSortedAndFilteredWidgets(RectangleFilter filter, Pageable pageable) {
    	List<WidgetEntity> filteredEntities =  sortedWidgets.stream().filter(w ->  WidgetStoreUtils.isWidgetInsideRectangle(w, filter)).collect(Collectors.toList());
    	return  WidgetStoreUtils.getPage(pageable, filteredEntities);
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
}
