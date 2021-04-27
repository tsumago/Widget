package com.boulifa.widget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.boulifa.widget.entity.Widget;
import com.boulifa.widget.errorhandler.ApiException;
import com.boulifa.widget.repository.WidgetRepository;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTests {
	@Mock
	private WidgetRepository widgetRepository;

	@InjectMocks
	private WidgetService widgetService = new WidgetServiceDatabase();
	
	private Widget widget;
	
	@BeforeEach
	private void init() {
		widget = new Widget();
		
		widget.setCenterX(0);
		widget.setCenterY(0);
		widget.setDepthZ(0);
		widget.setHeight(10);
		widget.setWidth(5);
	}

	@Test
	public void getWidget_WhenWidgetNotFound() {
		when(widgetRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
			widgetService.getWidget(1L);
		});

		assertEquals(thrown.getMessage(), "Widget #1 not found");
	}
	
	@Test
	public void getWidget_WhenHappyPath() {
		when(widgetRepository.findById(anyLong())).thenReturn(Optional.of(widget));
		
		Widget result = widgetService.getWidget(1L);

		assertEquals(result.getCenterX(), widget.getCenterX());
	}
	
	@Test
	public void getAllWidgets_WhenNoWidgetFound() {
		when(widgetRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<Widget>(new ArrayList<Widget>()));
		ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
			widgetService.getAllWidgets(1, 10);
		});

		assertEquals(thrown.getMessage(), "No Widget found");
	}
	
	@Test
	public void getAllWidgets_WhenHappyPath() {
		var list = Stream.of(widget).collect(Collectors.toList());
		when(widgetRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<Widget>(list));
		var result = widgetService.getAllWidgets(1, 10);

		assertEquals(result.size(), 1);
	}
	
	@Test
	public void editWidget_WhenHappyPath() {
		when(widgetRepository.findById(anyLong())).thenReturn(Optional.of(widget));
		when(widgetRepository.save(any(Widget.class))).thenReturn(widget);
		
		widgetService.editWidget(1L, widget);

		verify(widgetRepository, times(1)).save(any(Widget.class));
	}
	
	@Test
	public void editWidget_WhenWidgetNotFound() {
		when(widgetRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
			widgetService.editWidget(1L, widget);
		});

		assertEquals(thrown.getMessage(), "Widget #1 not found");
	}
}