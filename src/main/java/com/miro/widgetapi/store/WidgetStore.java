package com.miro.widgetapi.store;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.model.RectangleFilter;

public interface WidgetStore {
	public WidgetEntity save(WidgetEntity entity);
	public Optional<WidgetEntity> getWidget(String id);
	public void removeWidget(String id);
	public void shiftWidgetsAboveZUpwards(Integer z);
	public Integer getNextZ();
	Page<WidgetEntity> getSortedWidgets(Pageable pageable);
	Page<WidgetEntity> getSortedAndFilteredWidgets(RectangleFilter filter, Pageable pageable);
	public WidgetEntity update(WidgetEntity entity);
}
