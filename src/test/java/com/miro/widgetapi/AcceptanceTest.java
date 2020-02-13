package com.miro.widgetapi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widgetapi.helper.TestHelper;
import com.miro.widgetapi.model.PageInfo;
import com.miro.widgetapi.model.PagedWidget;
import com.miro.widgetapi.model.Widget;
import com.miro.widgetapi.model.WidgetSource;

abstract public class AcceptanceTest {
	
	private ObjectMapper mapper = new ObjectMapper();

	private MockMvc mockMvc;

	public AcceptanceTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	protected void run() throws Exception {
		
		//first insertion z null should have z = 0
		Widget first = convertResponseToWidget(postWidgetAndAssert(TestHelper.source(0, 0, null, 100, 100), 0));
		getWidgetAndAssert(first, 0);

		// z present should  z = passed value
		Widget second = convertResponseToWidget(postWidgetAndAssert(TestHelper.source(1, 1, 5, 10, 10), 5));
		getWidgetAndAssert(first, 0);
		getWidgetAndAssert(second, 5);

		// z null should have z = max(z) + 1
		Widget third = convertResponseToWidget(postWidgetAndAssert(TestHelper.source(-1, -1, null, 1000, 1000), 6));
		getWidgetAndAssert(first, 0);
		getWidgetAndAssert(second, 5);
		getWidgetAndAssert(third, 6);

		// z present and already exists then should have z = passed value by shifting upper widgets
		Widget fourth = convertResponseToWidget(postWidgetAndAssert(TestHelper.source(-1, -1, 5, 1000, 1000), 5));
		getWidgetAndAssert(first, 0);
		getWidgetAndAssert(second, 6);
		getWidgetAndAssert(third, 7);
		getWidgetAndAssert(fourth, 5);
		
		//get widgets and assert correct order
		PageInfo expectedPageInfo = new PageInfo(4, 1, 10, 0);
		PagedWidget widgets = convertResponseToPagedWidget(getWidgetsAndAssertPageInfo(expectedPageInfo));
		List<Widget> content = widgets.getContent();
		assertEquals(4, content.size());
		assertEquals(content.get(0).getId(), first.getId());
		assertEquals(content.get(1).getId(), fourth.getId());
		assertEquals(content.get(2).getId(), second.getId());
		assertEquals(content.get(3).getId(), third.getId());
		
		//get second page when pagesize is 2
		widgets = convertResponseToPagedWidget(getWidgetsAndAssertPageInfo(1, 2, new PageInfo(4, 2, 2, 1)));
		content = widgets.getContent();
		assertEquals(2, content.size());
		assertEquals(content.get(0).getId(), second.getId());
		assertEquals(content.get(1).getId(), third.getId());

	}
	

	private String postWidgetAndAssert(WidgetSource source, Integer expectedZ) throws Exception {
		String jsonBody = mapper.writeValueAsString(source);
		return mockMvc.perform(post("/widgets")
			 .content(jsonBody)
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isCreated())
      		 .andExpect(jsonPath("$.id").isNotEmpty())
      		 .andExpect(jsonPath("$.lastModification").isNotEmpty())
      		 .andExpect(jsonPath("$.x").value(source.getX()))
      		 .andExpect(jsonPath("$.y").value(source.getY()))
      		 .andExpect(jsonPath("$.z").value(expectedZ))
      		 .andExpect(jsonPath("$.width").value(source.getWidth()))
      		 .andExpect(jsonPath("$.height").value(source.getHeight()))
      		 .andReturn().getResponse().getContentAsString();
	}
	
	private String getWidgetAndAssert(Widget widget, Integer expectedZ) throws Exception {
		return mockMvc.perform(get("/widgets/" + widget.getId())
			  .accept(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isOk())
      		 .andExpect(jsonPath("$.id").isNotEmpty())
      		 .andExpect(jsonPath("$.lastModification").isNotEmpty())
      		 .andExpect(jsonPath("$.x").value(widget.getX()))
      		 .andExpect(jsonPath("$.y").value(widget.getY()))
      		 .andExpect(jsonPath("$.z").value(expectedZ))
      		 .andExpect(jsonPath("$.width").value(widget.getWidth()))
      		 .andExpect(jsonPath("$.height").value(widget.getHeight()))
      		 .andReturn().getResponse().getContentAsString();
	}
	
	private String getWidgetsAndAssertPageInfo(PageInfo pageInfo) throws Exception {
		return mockMvc.perform(get("/widgets")
			 .accept(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isOk())
      		 .andExpect(jsonPath("$.page.totalElements").value(pageInfo.getTotalElements()))
      		 .andExpect(jsonPath("$.page.totalPages").value(pageInfo.getTotalPages()))
      		 .andExpect(jsonPath("$.page.size").value(pageInfo.getSize()))
      		 .andExpect(jsonPath("$.page.number").value(pageInfo.getNumber()))
     		 .andReturn().getResponse().getContentAsString();
	}
	
	private String getWidgetsAndAssertPageInfo(int page, int size, PageInfo pageInfo) throws Exception {
		return mockMvc.perform(get("/widgets")
		     .queryParam("page", String.valueOf(page))
		     .queryParam("size", String.valueOf(size))
			 .accept(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isOk())
      		 .andExpect(jsonPath("$.page.totalElements").value(pageInfo.getTotalElements()))
      		 .andExpect(jsonPath("$.page.totalPages").value(pageInfo.getTotalPages()))
      		 .andExpect(jsonPath("$.page.size").value(pageInfo.getSize()))
      		 .andExpect(jsonPath("$.page.number").value(pageInfo.getNumber()))
     		 .andReturn().getResponse().getContentAsString();
	}
	
	private Widget convertResponseToWidget(String contentAsString) {
		try {
			return mapper.readValue(contentAsString, Widget.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("Conversion failed");
		}
		return null;
	};
	
	private PagedWidget convertResponseToPagedWidget(String contentAsString) {
		try {
			return mapper.readValue(contentAsString, PagedWidget.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("Conversion failed");
		}
		return null;
	};
	
}
