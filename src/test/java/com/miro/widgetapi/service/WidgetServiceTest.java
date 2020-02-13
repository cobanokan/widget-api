package com.miro.widgetapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.helper.UUIDProvider;
import com.miro.widgetapi.helper.TestHelper;
import com.miro.widgetapi.model.WidgetPatchSource;
import com.miro.widgetapi.model.WidgetSource;
import com.miro.widgetapi.store.WidgetStore;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {

	@Mock
	private UUIDProvider uuidProvider;

	@Mock
	private WidgetStore store;
	
	private WidgetService service;
	
	@BeforeEach
	public void init() {
		service = new WidgetService(uuidProvider, store);
	}
	
	@Test
	void addWidgetShouldGetNextZIfZIsNull() {
		WidgetSource source = TestHelper.simpleSource(null);
		
		when(uuidProvider.random()).thenReturn("generated-id");
		when(store.getNextZ()).thenReturn(42);
		when(store.save(ArgumentMatchers.any())).thenAnswer(returnsFirstArg());
		
		WidgetEntity addWidget = service.addWidget(source);
		assertEquals(42, addWidget.getZ());
		
		verify(store).getNextZ();
		verify(store).save(ArgumentMatchers.any());
		verify(uuidProvider).random();
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}

	@Test
	void addWidgetShouldShiftWidgetsWithBiggerZUpwardsIfZIsNotNull() {
		WidgetSource source = TestHelper.simpleSource(42);
		
		when(uuidProvider.random()).thenReturn("generated-id");
		when(store.save(ArgumentMatchers.any())).thenAnswer(returnsFirstArg());
		
		WidgetEntity addWidget = service.addWidget(source);
		assertEquals(42, addWidget.getZ());
		
		verify(store).shiftWidgetsAboveZUpwards(42);
		verify(store).save(ArgumentMatchers.any());
		verify(uuidProvider).random();
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}
	
	@Test
	void getWidgetShouldThrowNotFoundIfWidgetIsNotPresent() {
		String invalidId = "not-existing";
		when(store.getWidget(invalidId)).thenReturn(Optional.empty());
		
		try {
			service.getWidget(invalidId);
		} catch (Exception e) {
			assertTrue(e instanceof WidgetNotFoundException);
		}

		verify(store).getWidget(invalidId);
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}
	
	@Test
	void updateWidgetShouldShiftWidgetsUpwardsIfZIsUpdated() throws WidgetNotFoundException {
		WidgetEntity widget = TestHelper.simpleEntityWithZ(123);		
		Instant givenLastModifiedYesterday = Instant.now().minus(Duration.ofDays(1));
		widget.setLastModification(Date.from(givenLastModifiedYesterday));
		
		when(store.getWidget(widget.getId())).thenReturn(Optional.of(new WidgetEntity(widget)));
		when(store.update(ArgumentMatchers.any())).thenAnswer(returnsFirstArg());
		
		WidgetPatchSource widgetPatchSource = new WidgetPatchSource();
		widgetPatchSource.setZ(0);
		
		WidgetEntity updatedWidget = service.updateWidget(widget.getId(), widgetPatchSource);
		
		assertEquals(0, updatedWidget.getZ());
		assertEquals(widget.getX(), updatedWidget.getX());
		assertEquals(widget.getY(), updatedWidget.getY());
		assertEquals(widget.getHeight(), updatedWidget.getHeight());
		assertEquals(widget.getWidth(), updatedWidget.getWidth());
		assertEquals(widget.getId(), updatedWidget.getId());
		assertTrue(updatedWidget.getLastModification().after(widget.getLastModification()));
		
		verify(store).shiftWidgetsAboveZUpwards(0);
		verify(store).update(ArgumentMatchers.any());
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}
	
	@Test
	void updateWidgetShouldNotShifftWidgetsUpwardsIfZIsNotUpdated() throws WidgetNotFoundException {
		WidgetEntity widget = TestHelper.simpleEntityWithZ(123);		
		Instant givenLastModifiedYesterday = Instant.now().minus(Duration.ofDays(1));
		widget.setLastModification(Date.from(givenLastModifiedYesterday));
		
		when(store.getWidget(widget.getId())).thenReturn(Optional.of(new WidgetEntity(widget)));
		when(store.update(ArgumentMatchers.any())).thenAnswer(returnsFirstArg());
		
		WidgetPatchSource widgetPatchSource = new WidgetPatchSource();
		int newX = 100;
		widgetPatchSource.setX(newX);
		int newY = 200;
		widgetPatchSource.setY(newY);
		int newWidth = 90;
		widgetPatchSource.setWidth(newWidth);
		int newHeigth = 120;
		widgetPatchSource.setHeight(newHeigth);

		WidgetEntity updatedWidget = service.updateWidget(widget.getId(), widgetPatchSource);
		
		assertEquals(widget.getZ(), updatedWidget.getZ());
		assertEquals(newX, updatedWidget.getX());
		assertEquals(newY, updatedWidget.getY());
		assertEquals(newWidth, updatedWidget.getWidth());
		assertEquals(newHeigth, updatedWidget.getHeight());
		assertEquals(widget.getId(), updatedWidget.getId());
		assertTrue(updatedWidget.getLastModification().after(widget.getLastModification()));
		
		verify(store).update(ArgumentMatchers.any());
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}
	
	@Test
	void updateWidgetShouldThrowNotFoundIfWidgetIsNotPresent() {
		String invalidId = "not-existing";
		when(store.getWidget(invalidId)).thenReturn(Optional.empty());
		
		try {
			service.updateWidget(invalidId, new WidgetPatchSource());
		} catch (Exception e) {
			assertTrue(e instanceof WidgetNotFoundException);
		}

		verify(store).getWidget(invalidId);
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}
	
	@Test
	void deleteShouldRemoveWidgetFromStore() throws WidgetNotFoundException {
		WidgetEntity entity = TestHelper.simpleEntityWithZ(123);
		when(store.getWidget(entity.getId())).thenReturn(Optional.of(entity));
		
		service.removeWidget(entity.getId());
		
		verify(store).getWidget(entity.getId());
		verify(store).removeWidget(entity.getId());
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}
	
	@Test
	void deleteWidgetShouldThrowNotFoundIfWidgetIsNotPresent() {
		String invalidId = "not-existing";
		when(store.getWidget(invalidId)).thenReturn(Optional.empty());
		
		try {
			service.removeWidget(invalidId);
		} catch (Exception e) {
			assertTrue(e instanceof WidgetNotFoundException);
		}

		verify(store).getWidget(invalidId);
		verifyNoMoreInteractions(store);
		verifyNoMoreInteractions(uuidProvider);
	}

}
