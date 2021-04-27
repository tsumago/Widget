package com.boulifa.widget.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boulifa.widget.entity.Widget;
import com.boulifa.widget.service.WidgetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/v1")
public class WidgetController {
	
	@Autowired
	private WidgetService widgetService;
	
	@Value("${page.size.max}")
	private int defaultPageSize;
	
	private final Logger logger = LoggerFactory.getLogger(WidgetController.class);
	
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public Widget createWidget(@RequestBody @Valid Widget newWidget) {
		logger.debug("Creating new Widget {}", newWidget.toString());
		return widgetService.createWidget(newWidget);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public Widget getWidget(@PathVariable Long id) {
		logger.debug("Fetching Widget #{}", id);
		return widgetService.getWidget(id);
	}
	
	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<Widget> getAllWidgets(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		pageSize = pageSize > this.defaultPageSize ? this.defaultPageSize : pageSize;
		logger.debug("Fetching widgets: Page {} (Widgets per page {})", pageNo, pageSize);
		return widgetService.getAllWidgets(pageNo, pageSize);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public Widget editWidget(@RequestBody Widget updatedWidget, @PathVariable Long id) {
		logger.debug("Editing Widget #{}", id);		
		return widgetService.editWidget(id, updatedWidget);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public void deleteWidget(@PathVariable Long id) {
		logger.debug("Deleting Widget #{}", id);
		widgetService.deleteById(id);
	}
}
