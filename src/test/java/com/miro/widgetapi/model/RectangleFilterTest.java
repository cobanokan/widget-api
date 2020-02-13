package com.miro.widgetapi.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.miro.widgetapi.exception.InvalidRectangleFilterException;

class RectangleFilterTest {

	@Test
	void shouldBeValidToUseIfAllValuesPresentAndValid() throws InvalidRectangleFilterException {
		assertTrue(new RectangleFilter(0, 0, 10, 10).isValidToUse());
	}

	@Test
	void shouldNotBeValidToUseIfNoValuesPresent() throws InvalidRectangleFilterException {
		assertFalse(new RectangleFilter(null, null, null, null).isValidToUse());
	}

	@Test
	void shouldFailToCheckIfSomeValuesAreMissing() {
	  Assertions.assertThrows(InvalidRectangleFilterException.class, () -> {
		new RectangleFilter(null, 0, 10, 10).isValidToUse();
	  });
	}
	
	@Test
	void shouldFailToCheckIfMinXIsMoreThanMaxX() {
	  Assertions.assertThrows(InvalidRectangleFilterException.class, () -> {
		new RectangleFilter(100, 0, 0, 10).isValidToUse();
	  });
	}

	@Test
	void shouldFailToCheckIfMinYIsMoreThanMaxY() {
	  Assertions.assertThrows(InvalidRectangleFilterException.class, () -> {
		new RectangleFilter(0, 100, 10, 0).isValidToUse();
	  });
	}
}
