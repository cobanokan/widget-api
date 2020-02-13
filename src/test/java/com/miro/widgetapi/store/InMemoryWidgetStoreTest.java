package com.miro.widgetapi.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.helper.TestHelper;
import com.miro.widgetapi.store.InMemoryWidgetStore;

class InMemoryWidgetStoreTest {

	private static final int DEFAULT_Z = 0;
	private WidgetStore store;
	
	@BeforeEach
	public void before() {
		store = new InMemoryWidgetStore();
	}

	@Test
	void afterAddWidgetRepositoryShouldContainWidget() {
		WidgetEntity entity = TestHelper.simpleEntityWithZ(42);
		
		WidgetEntity widget = store.save(entity);

		assertHavingSameValues(entity, widget);
		
		Optional<WidgetEntity> getWidget = store.getWidget(entity.getId());
		assertTrue(getWidget.isPresent());
		assertHavingSameValues(widget, getWidget.get());
			
		Page<WidgetEntity> widgets = store.getWidgetsSortedByZ(PageRequest.of(0, 10));
		assertEquals(1, widgets.getTotalElements());
		assertHavingSameValues(widget, widgets.get().iterator().next());
	}

	@Test
	void afterRemoveWidgetRepositoryShouldNotContainWidget() {
		WidgetEntity entity = TestHelper.simpleEntityWithZ(42);
		
		store.removeWidget(entity.getId());
		
		Optional<WidgetEntity> getWidget = store.getWidget(entity.getId());
		assertFalse(getWidget.isPresent());
		
		assertEquals(0, store.getWidgetsSortedByZ(PageRequest.of(0, 10)).getTotalElements());
	}
	
	@Test
	void givenWidgetsEmptyGetNextZShouldReturnDefaultZ() {
		assertEquals(DEFAULT_Z, store.getNextZ());	
	}
	
	@Test
	void getNextZShouldReturnMaxZPlusOne() {
		WidgetEntity widgetAt0 = TestHelper.simpleEntityWithZ(0);
		store.save(widgetAt0);

		assertEquals(widgetAt0.getZ() + 1, store.getNextZ());
		
		WidgetEntity widgetAt5 = TestHelper.simpleEntityWithZ(5);
		store.save(widgetAt5);
		
		assertEquals(widgetAt5.getZ() + 1, store.getNextZ());
	}

	@Test
	void shouldPushWidgetsWithBiggerZUpdwards() {
		WidgetEntity widgetAt0 = TestHelper.simpleEntityWithZ(0);
		WidgetEntity widgetAt3 = TestHelper.simpleEntityWithZ(3);
		WidgetEntity widgetAt5 = TestHelper.simpleEntityWithZ(5);
		
		store.save(widgetAt0);
		store.save(widgetAt3);
		store.save(widgetAt5);

		store.shiftWidgetsAboveZUpwards(3);
		
		WidgetEntity widget0AfterNewEntity = store.getWidget(widgetAt0.getId()).get();
		assertEquals(widgetAt0.getZ(), widget0AfterNewEntity.getZ());
		
		WidgetEntity widget3AfterNewEntity = store.getWidget(widgetAt3.getId()).get();
		assertEquals(widgetAt3.getZ() + 1, widget3AfterNewEntity.getZ());
		
		WidgetEntity widget5AfterNewEntity = store.getWidget(widgetAt5.getId()).get();
		assertEquals(widgetAt5.getZ() + 1, widget5AfterNewEntity.getZ());
	}
	
	private void assertHavingSameValues(WidgetEntity entity, WidgetEntity widget) {
		assertEquals(entity.getId(), widget.getId());
		assertEquals(entity.getHeight(), widget.getHeight());
		assertEquals(entity.getWidth(), widget.getWidth());
		assertEquals(entity.getX(), widget.getX());
		assertEquals(entity.getY(), widget.getY());
		assertEquals(entity.getZ(), widget.getZ());
		assertEquals(entity.getLastModification(), widget.getLastModification());
	}
}
