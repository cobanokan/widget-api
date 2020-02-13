package com.miro.widgetapi.model;

import static com.miro.widgetapi.model.Widget.widgetFromEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.miro.widgetapi.entity.WidgetEntity;

public class PagedWidget {
	
	public List<Widget> content;
	public PageInfo page;
	
	public PagedWidget(Page<WidgetEntity> page) {
		this.content = new ArrayList<Widget>();
		page.forEach(entity -> this.content.add(widgetFromEntity(entity)));
		this.page = new PageInfo(page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber());
	}
	
	public List<Widget> getContent() {
		return content;
	}
	
	public PageInfo getPage() {
		return page;
	}
}
