package com.miro.widgetapi.controller;

import static com.miro.widgetapi.model.Widget.widgetFromEntity;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.miro.widgetapi.exception.InvalidRectangleFilterException;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.exception.WidgetPageSizeTooBigException;
import com.miro.widgetapi.model.PagedWidget;
import com.miro.widgetapi.model.RectangleFilter;
import com.miro.widgetapi.model.Widget;
import com.miro.widgetapi.model.WidgetPatchSource;
import com.miro.widgetapi.model.WidgetSource;
import com.miro.widgetapi.service.WidgetService;

@RestController
@RequestMapping("widgets")
public class WidgetController {
	Logger logger = LogManager.getLogger(WidgetController.class);
	
	private WidgetService service;

	@Autowired
	public WidgetController(WidgetService service) {
		this.service = service;
	}
	
	@GetMapping
	public PagedWidget getWidgets(@PageableDefault(size = 10, page = 0) Pageable pageable, RectangleFilter filter) throws WidgetPageSizeTooBigException, InvalidRectangleFilterException {
		logger.info("Getting widgets page {}", pageable);	
		return new PagedWidget(service.getWidgets(filter, pageable));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Widget createWidget(@RequestBody @Valid WidgetSource source) {
		logger.info("Creating widget", source);
		return widgetFromEntity(service.addWidget(source));
	}
	
	@GetMapping(value = "/{id}")
	public Widget getWidget(@PathVariable(value = "id") String id) throws WidgetNotFoundException {
		logger.info("Getting widget {}", id);
		return widgetFromEntity(service.getWidget(id));
	}
	
	@PatchMapping(value = "/{id}")
	public Widget updateWidget(
			@PathVariable(value = "id") String id,
			@RequestBody @Valid WidgetPatchSource source) throws WidgetNotFoundException {
		logger.info("Updating widget {}", id);
		return widgetFromEntity(service.updateWidget(id, source));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/{id}")
	public void removeWidget(@PathVariable(value = "id") String id) throws WidgetNotFoundException {
		logger.info("Removing widget {}", id);
		service.removeWidget(id);
	}

}
