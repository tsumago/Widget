package com.boulifa.widget.controller.v1;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.boulifa.widget.entity.Widget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class WidgetControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	private static ObjectWriter objectWriter;
	
	@BeforeAll
	private static void init() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    objectWriter = mapper.writer().withDefaultPrettyPrinter();
	}
	
	@Test
	@Order(1)
	public void createWidget_WhenHappyPath() {
		Widget widget = new Widget();
		widget.setCenterX(0);
		widget.setCenterY(0);
		widget.setDepthZ(0);
		widget.setHeight(10);
		widget.setWidth(5);
		
		try {
			mockMvc.perform(post("/v1/create")
					.contentType("application/json")
			        .content(objectWriter.writeValueAsString(widget)))
			        .andExpect(status().isCreated());
		} catch (Exception e) {
			fail("Widget creation happy path test failed with an exception");
		}
	}
	
	@Test
	@Order(2)
	public void createWidget_WhenInvalidWidget() {
		Widget widget = new Widget();
		widget.setCenterX(0);
		widget.setCenterY(0);
		widget.setDepthZ(0);
		widget.setHeight(-10);
		widget.setWidth(5);
		
		try {
			mockMvc.perform(post("/v1/create")
					.contentType("application/json")
			        .content(objectWriter.writeValueAsString(widget)))
			        .andExpect(status().isBadRequest());
		} catch (Exception e) {
			fail("Widget creation with invalid input test failed with an exception");
		}
	}
	
	@Test
	@Order(3)
	public void createWidget_WhenWithoutDepthZ() {
		Widget widget = new Widget();
		widget.setCenterX(0);
		widget.setCenterY(0);
		widget.setHeight(10);
		widget.setWidth(5);
		
		try {
			mockMvc.perform(post("/v1/create")
					.contentType("application/json")
			        .content(objectWriter.writeValueAsString(widget)))
			        .andExpect(status().isCreated())
			        .andExpect(jsonPath("$.depthZ").value(1));
		} catch (Exception e) {
			fail("Wifget creation without depthZ test failed with an exception");
		}
	}
	
	@Test
	@Order(4)
	public void getWidget_WhenHappyPath() {
		
		try {
			mockMvc.perform(get("/v1/1")
					.contentType("application/json"))
			        .andExpect(status().isOk())
			        .andExpect(jsonPath("$.depthZ").value(0));
		} catch (Exception e) {
			fail("Widget fetch Happy Path test failed with an exception");
		}
	}
	
	@Test
	@Order(5)
	public void getWidget_WhenWidgetNotExistant() {
		
		try {
			mockMvc.perform(get("/v1/10")
					.contentType("application/json"))
			        .andExpect(status().isNotFound());
		} catch (Exception e) {
			fail("Inexistant Widget fetch test failed with an exception");
		}
	}
	
	@Test
	@Order(6)
	public void getAllWidget_WhenHappyPath() {
		
		try {
			mockMvc.perform(get("/v1/all")
					.contentType("application/json"))
			        .andExpect(jsonPath("$.[0].depthZ").value(0))
			        .andExpect(status().isOk());
		} catch (Exception e) {
			fail("Fetch All Happy path test failed with an exception");
		}
	}
	
	@Test
	@Order(7)
	public void editWidget_WhenHappyPath() {
		Widget widget = new Widget();
		widget.setCenterX(7);
		widget.setCenterY(7);
		widget.setHeight(7);
		widget.setWidth(7);
		widget.setDepthZ(7);
		
		try {
			mockMvc.perform(put("/v1/1")
					.contentType("application/json")
					.content(objectWriter.writeValueAsString(widget)))
			        .andExpect(jsonPath("$.depthZ").value(7))
			        .andExpect(status().isAccepted());
		} catch (Exception e) {
			fail("Update Widget Happy Path test failed with an exception");
		}
	}
	
	@Test
	@Order(8)
	public void deleteWidget_WhenHappyPath() {
		
		try {
			mockMvc.perform(delete("/v1/1")
					.contentType("application/json"))
			        .andExpect(status().isOk());
		} catch (Exception e) {
			fail("Delete Widget Happy Path test failed with an exception");
		}
	}
}
