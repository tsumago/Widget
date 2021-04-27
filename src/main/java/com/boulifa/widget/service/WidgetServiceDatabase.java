package com.boulifa.widget.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.boulifa.widget.entity.Widget;
import com.boulifa.widget.errorhandler.ApiException;
import com.boulifa.widget.repository.WidgetRepository;

@Service
@Profile("database")
public class WidgetServiceDatabase implements WidgetService {
	
	@Autowired
	private WidgetRepository widgetRepo;
	
	@Override
	public Widget getWidget(Long id) {
		var response =  widgetRepo.findById(id).orElse(null);
		
		if (response == null) {
			throw new ApiException("Widget #" + id + " not found", HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@Override
	public List<Widget> getAllWidgets(Integer pageNo, Integer pageSize) {
		var paging = PageRequest.of(pageNo, pageSize, Sort.by("depthZ").ascending());
		 
        var pagedResult = widgetRepo.findAll(paging);		
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
        	throw new ApiException("No Widget found", HttpStatus.NOT_FOUND);
        }
	}
	
	@Override
	public Widget createWidget(Widget newWidget) {
		if (newWidget.getDepthZ() == null) {
			newWidget.setDepthZ(widgetRepo.getMaxDepthZ() + 1);
		}
		
		updateDepthZ(newWidget.getDepthZ());
		
		return widgetRepo.save(newWidget);
	}
	
	@Override
	public Widget editWidget(Long id, Widget updatedWidget) {
		
		var existingWidget = widgetRepo.findById(id).orElse(null);
		
		if (existingWidget == null) {
			throw new ApiException("Widget #" + id + " not found", HttpStatus.NOT_FOUND);
		}
		
		existingWidget.update(updatedWidget);
		
		if (updatedWidget.getDepthZ() == null) {
			existingWidget.setDepthZ(widgetRepo.getMaxDepthZ() + 1);
		}
		
		updateDepthZ(existingWidget.getDepthZ());
		
		return widgetRepo.save(existingWidget);
	}

	@Override
	public void deleteById(Long id) {
		widgetRepo.deleteById(id);
	}
	
	private void updateDepthZ(int depthZ) {
		var widgets = widgetRepo.findByDepthZGreaterThanEqualOrderByDepthZAsc(depthZ);
		
		if (widgets != null) {
			for (int i = 0;i < widgets.size();i++) {
				
				if (i + 1 < widgets.size() && widgets.get(i + 1).getDepthZ() > widgets.get(i).getDepthZ() + 1) {
					break;
				} else {
					widgets.get(i).setDepthZ(widgets.get(i).getDepthZ() + 1);
				}
			}
			
			widgetRepo.saveAll(widgets);
		}
	}
}
