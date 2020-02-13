package com.miro.widgetapi.model;

import static com.miro.widgetapi.model.Widget.widgetFromEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.miro.widgetapi.entity.WidgetEntity;

public class PagedWidget {
	
	private List<Widget> content;
	private PageInfo page;
	
	public PagedWidget() {
	}
	
	public PagedWidget(Page<WidgetEntity> page) {
		this.setContent(new ArrayList<Widget>());
		page.forEach(entity -> this.getContent().add(widgetFromEntity(entity)));
		this.setPage(new PageInfo(page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber()));
	}
	
	public List<Widget> getContent() {
		return content;
	}
	
	public PageInfo getPage() {
		return page;
	}

	public void setContent(List<Widget> content) {
		this.content = content;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}
}
