package com.matmr.restaurantpoll.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class HomeControllerTest {

	@InjectMocks
	private HomeController homeController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(homeController)
				.setRemoveSemicolonContent(false).build();

	}

	@Test
	public void home_ShouldRenderHome() throws Exception {
		this.mockMvc.perform(get("/index"))
		.andExpect(status().isOk())
		.andExpect(view().name("home"));
		
		this.mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("home"));
	}

}
