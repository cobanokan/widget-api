package com.miro.widgetapi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.miro.widgetapi.store.H2WidgetStore;
import com.miro.widgetapi.store.WidgetStore;

@SpringBootTest(properties = "h2.enabled=true")
@AutoConfigureMockMvc
class H2EnabledApplicationTest extends AcceptanceTest {

	@Autowired
	private WidgetStore store;
	
	@Autowired
	public H2EnabledApplicationTest(MockMvc mockMvc) {
		super(mockMvc);
	}
	
	@Test
	void repositoryShouldBeInH2WidgetStore() {
		assertTrue(store instanceof H2WidgetStore);
	}
	
	@Test
	protected void acceptance() throws Exception {
		super.run();
	}

}
