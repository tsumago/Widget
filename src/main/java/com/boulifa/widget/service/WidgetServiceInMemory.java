package com.boulifa.widget.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.boulifa.widget.entity.Widget;
import com.boulifa.widget.errorhandler.ApiException;

@Service
@Profile("!database")
public class WidgetServiceInMemory implements WidgetService {
	
	private List<Widget> widgets;
	private int idSequence;
	
	public WidgetServiceInMemory() {
		this.widgets = new ArrayList<>();
		this.idSequence = 1;
	}

	@Override
	public Widget getWidget(Long id) {
		var response =  widgets.stream().filter(widget -> widget.getId().equals(id)).findFirst().orElse(null);
		
		if (response == null) {
			throw new ApiException("Widget #" + id + " not found", HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

	@Override
	public List<Widget> getAllWidgets(Integer pageNo, Integer pageSize) {
		var beginningIndex = pageSize * pageNo >= widgets.size() ? 0 : pageSize * pageNo;
		var endIndex = pageSize * pageNo + pageSize >= widgets.size() ? widgets.size() : pageSize * pageNo + pageSize;
		if (widgets.isEmpty()) {
			beginningIndex = 0;
			endIndex = 1;
		}
		
		widgets.sort((w1, w2) -> w1.getDepthZ().compareTo(w2.getDepthZ()));
		
		var response =  widgets.subList(beginningIndex, endIndex);
		
		if (response == null || response.isEmpty()) {
			throw new ApiException("No Widget found", HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@Override
	public Widget createWidget(Widget newWidget) {
		if (newWidget.getDepthZ() == null) {
			var maxDepth = widgets.stream().max(Comparator.comparing(Widget::getDepthZ)).orElse(null);
			if (maxDepth == null) {
				newWidget.setDepthZ(0);
			} else {
				newWidget.setDepthZ(maxDepth.getDepthZ() + 1);
			}
		}
		
		newWidget.setId(Long.valueOf(idSequence++));
		
		newWidget.setLasModified(LocalDateTime.now());
		
		updateDepthZ(newWidget.getDepthZ());
		
		widgets.add(newWidget);
		
		return newWidget;
	}

	@Override
	public Widget editWidget(Long id, Widget updatedWidget) {
		var existingWidget = widgets.stream().filter(widget -> widget.getId() == id).findAny().orElse(null);
		
		if (existingWidget == null) {
			throw new ApiException("Widget #" + id + " not found", HttpStatus.NOT_FOUND);
		}
		
		existingWidget.update(updatedWidget);
		
		existingWidget.setLasModified(LocalDateTime.now());
		
		if (updatedWidget.getDepthZ() == null) {
			var maxdepth = widgets.stream().max(Comparator.comparing(Widget::getDepthZ)).orElse(null);
			existingWidget.setDepthZ(maxdepth.getDepthZ() + 1);
		}
		
		if (widgets.stream().filter(widget -> widget.getId() != id).anyMatch(widget -> widget.getDepthZ().equals(existingWidget.getDepthZ()))) {
			updateDepthZ(existingWidget.getDepthZ());
		}
		
		return existingWidget;
	}

	@Override
	public void deleteById(Long id) {
		widgets.removeIf(widget -> widget.getId() == id);
	}
	
	private void updateDepthZ(int depthZ) {
		var sortedWidgets = widgets.stream().filter(widget -> widget.getDepthZ() >= depthZ).sorted(Comparator.comparingInt(Widget::getDepthZ)).collect(Collectors.toList());
		
		if (sortedWidgets != null) {
			for (int i = 0;i < sortedWidgets.size();i++) {
				
				if (i + 1 < sortedWidgets.size() && sortedWidgets.get(i + 1).getDepthZ() > sortedWidgets.get(i).getDepthZ() + 1) {
					break;
				} else {
					sortedWidgets.get(i).setDepthZ(sortedWidgets.get(i).getDepthZ() + 1);
				}
			}
		}
	}
}
