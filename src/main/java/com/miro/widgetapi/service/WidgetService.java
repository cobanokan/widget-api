package com.miro.widgetapi.service;

import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.exception.InvalidRectangleFilterException;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.exception.WidgetPageSizeTooBigException;
import com.miro.widgetapi.helper.UUIDProvider;
import com.miro.widgetapi.model.RectangleFilter;
import com.miro.widgetapi.model.WidgetPatchSource;
import com.miro.widgetapi.model.WidgetSource;
import com.miro.widgetapi.store.WidgetStore;

@Component
public class WidgetService {
	
	Logger logger = LogManager.getLogger(WidgetService.class);
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private UUIDProvider uuidProvider;
	private WidgetStore store;

	@Autowired
	public WidgetService(UUIDProvider uuidProvider, WidgetStore widgetRepository) {
		this.uuidProvider = uuidProvider;
		this.store = widgetRepository;
	}
	
	public WidgetEntity addWidget(WidgetSource source) {
		logger.info("adding new widget to store");
		WidgetEntity entity = new WidgetEntity();
		entity.setId(uuidProvider.random());
		entity.setX(source.getX());
		entity.setY(source.getY());
		entity.setZ(source.getZ());
		entity.setHeight(source.getHeight());
		entity.setWidth(source.getWidth());
		entity.setLastModification(new Date());
	
		try {
			lock.writeLock().lock();
			if(entity.getZ() != null) {
				logger.info("Z is {} shifts widgets", entity.getZ());
				store.shiftWidgetsAboveZUpwards(entity.getZ());	
			} else {
				logger.info("Z is null put widget to the top");
				entity.setZ(store.getNextZ());
			}
			return store.save(entity);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Page<WidgetEntity> getWidgets(RectangleFilter filter, Pageable pageable) throws InvalidRectangleFilterException, WidgetPageSizeTooBigException {
		if(pageable.getPageSize() > 500) {
			throw new WidgetPageSizeTooBigException(pageable.getPageSize());
		}
			
		try {
			lock.readLock().lock();
			logger.info("getting requested widgets page {} filter {}", pageable, filter);
			if(filter.isValidToUse()) {
				return store.getWidgetsSortedByZAndFiltered(filter, pageable);
			} else {
				return store.getWidgetsSortedByZ(pageable);
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	public WidgetEntity getWidget(String id) throws WidgetNotFoundException {
		try {
			logger.info("getting requested widget {}", id);
			lock.readLock().lock();
			return store.getWidget(id).orElseThrow(() -> new WidgetNotFoundException(id));
		} finally {
			lock.readLock().unlock();
		}
	}

	public WidgetEntity updateWidget(String id, WidgetPatchSource source) throws WidgetNotFoundException {
		try {
			logger.info("updating widget {}", id);

			lock.writeLock().lock();
			
			WidgetEntity widget = getWidget(id);
			if(source.getX().isPresent()) {
				widget.setX(source.getX().get());
			}
			
			if(source.getY().isPresent()) {
				widget.setY(source.getY().get());
			}
			
			if(source.getWidth().isPresent()) {
				widget.setWidth(source.getWidth().get());
			}
			
			if(source.getHeight().isPresent()) {
				widget.setHeight(source.getHeight().get());
			}
			
			boolean zIndexUpdated = false;
			if(source.getZ().isPresent()) {
				if(widget.getZ() != source.getZ().get()) {
					zIndexUpdated = true;
				}
				widget.setZ(source.getZ().get());
			}
			
			widget.setLastModification(new Date());

			if(zIndexUpdated) {
				logger.info("Z is updated shift widgets updwards");
				store.shiftWidgetsAboveZUpwards(widget.getZ());
			}
			return store.update(widget);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void removeWidget(String id) throws WidgetNotFoundException {
		try {
			logger.info("removing widget {}", id);
			lock.writeLock().lock();
			getWidget(id);
			store.removeWidget(id);
		} finally {
			lock.writeLock().unlock();
		}
	}
}
