package com.miro.widgetapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.helper.TestHelper;
import com.miro.widgetapi.model.WidgetPatchSource;
import com.miro.widgetapi.model.WidgetSource;
import com.miro.widgetapi.service.WidgetService;

@WebMvcTest(WidgetController.class)
class WidgetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WidgetService widgetService;

    @Test
    public void getWidgetsShouldReturnStatusOk() throws Exception {
    	when(widgetService.getWidgets(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new PageImpl<WidgetEntity>(Collections.emptyList()));

		mvc.perform(get("/widgets")
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isOk());
    }
  
    @Test
    public void getWidgetShouldReturnStatusOk() throws Exception {
    	when(widgetService.getWidget("valid-id")).thenReturn(TestHelper.simpleEntityWithZ(123));
		
		mvc.perform(get("/widgets/valid-id")
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isOk());
    }
    
    @Test
    public void getWidgetForUnknownIdShouldReturnStatusNotFound() throws Exception {
    	String unexistingId = "invalid-id";
		when(widgetService.getWidget(unexistingId)).thenThrow(new WidgetNotFoundException(unexistingId));
		
		mvc.perform(get("/widgets/invalid-id")
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isNotFound());
    }
    
    @Test
    public void postWidgetWithInvalidValuesShouldReturnStatusBadRequest() throws Exception {
    	WidgetSource source = new WidgetSource();
    	
		String jsonBody = new ObjectMapper().writeValueAsString(source);
		mvc.perform(post("/widgets")
			 .content(jsonBody)
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isBadRequest());
    }
    
    @Test
    public void updateWidgetWithInvalidValuesShouldReturnStatusBadRequest() throws Exception {
    	WidgetPatchSource source = new WidgetPatchSource();
    	
		String jsonBody = new ObjectMapper().writeValueAsString(source);
		mvc.perform(patch("/widgets/someid")
			 .content(jsonBody)
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isBadRequest());
    }
    
    @Test
    public void createShouldReturnStatusCreated() throws Exception {
		when(widgetService.addWidget(ArgumentMatchers.any(WidgetSource.class))).thenReturn(TestHelper.simpleEntityWithZ(123));
    	
		WidgetSource source = TestHelper.simpleSource(null);
		String jsonBody = new ObjectMapper().writeValueAsString(source);
		
		mvc.perform(post("/widgets")
			 .content(jsonBody)
		     .contentType(MediaType.APPLICATION_JSON))
      		 .andExpect(status().isCreated());
    }
}
