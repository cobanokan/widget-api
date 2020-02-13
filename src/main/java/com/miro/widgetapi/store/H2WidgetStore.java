package com.miro.widgetapi.store;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.model.RectangleFilter;
import com.miro.widgetapi.repository.WidgetRepository;


@ConditionalOnProperty(name = "h2.enabled", havingValue = "true")
@Component
public class H2WidgetStore implements WidgetStore {

	private static final int DEFAULT_Z = 0;
	private WidgetRepository repo;
	
	@Autowired
	public H2WidgetStore(WidgetRepository repo) {
		this.repo = repo;
	}

	@Override
	public WidgetEntity save(WidgetEntity entity) {
		return repo.save(entity);
	}

	@Override
	public Optional<WidgetEntity> getWidget(String id) {
		return repo.findById(id);
	}

	@Override
	public void removeWidget(String id) {
		repo.deleteById(id);
	}

	@Transactional
	@Override
	public void shiftWidgetsAboveZUpwards(Integer z) {
		repo.shiftWidgetsAboveZUpdwards(z, new Date());
	}

	@Override
	public Integer getNextZ() {
		Optional<WidgetEntity> lastZ = repo.findFirstByOrderByZDesc();
		return lastZ.isPresent() ? lastZ.get().getZ() + 1 : DEFAULT_Z;
	}

	@Override
	public Page<WidgetEntity> getSortedWidgets(Pageable pageable) {
		return repo.findAllByOrderByZAsc(pageable);
	}

	@Override
	public WidgetEntity update(WidgetEntity entity) {
		return repo.save(entity);
	}

	@Override
	public Page<WidgetEntity> getSortedAndFilteredWidgets(RectangleFilter filter, Pageable pageable) {
		return repo.findFilteredAndOrdered(filter, pageable);
	}

}


