package com.boulifa.widget.service;

import java.util.List;

import com.boulifa.widget.entity.Widget;

public interface WidgetService {

	Widget getWidget(Long id);

	List<Widget> getAllWidgets(Integer pageNo, Integer pageSize);

	Widget editWidget(Long id, Widget updatedWidget);
	
	Widget createWidget(Widget newWidget);

	void deleteById(Long id);	
}