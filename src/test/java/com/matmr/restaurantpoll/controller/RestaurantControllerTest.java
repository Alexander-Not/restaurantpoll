package com.matmr.restaurantpoll.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.matmr.restaurantpoll.model.Category;
import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.filter.RestaurantFilter;
import com.matmr.restaurantpoll.service.RestaurantService;
import com.matmr.restaurantpoll.util.RestaurantBuilder;
import com.matmr.restaurantpoll.util.TestUtil;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@WebAppConfiguration
public class RestaurantControllerTest {

	@Mock
	private RestaurantService restaurantService;

	@InjectMocks
	private RestaurantController restaurantController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).setRemoveSemicolonContent(false).build();

	}

	@Test
	public void searchByName_ShouldSearchWithBlankFilter() throws Exception {

		RestaurantFilter filter = new RestaurantFilter();
		filter.setName(null);

		Restaurant first = new RestaurantBuilder()
				.id(1L)
				.name("Abra")
				.description("lots of food")
				.category(Category.ITALIAN).build();

		Restaurant second = new RestaurantBuilder()
				.id(2L)
				.name("Kadabra")
				.description("food for days")
				.category(Category.PIZZA).build();

		when(restaurantService.findByNameIgnoreCaseContaining(filter)).thenReturn(Arrays.asList(first, second));

		this.mockMvc.perform(get("/restaurants"))
			.andExpect(status().isOk())
			.andExpect(view().name("restaurantList"))
			.andExpect(model().attribute("restaurants", hasSize(2)))
			.andExpect(model().attribute("restaurants",
					hasItem(allOf(
							hasProperty("id", is(1L)),
							hasProperty("name", is("Abra")),
							hasProperty("description", is("lots of food")),
							hasProperty("category", is(Category.ITALIAN))
							))))
			.andExpect(model().attribute("restaurants",
					hasItem(allOf(
							hasProperty("id", is(2L)),
							hasProperty("name", is("Kadabra")),
							hasProperty("description", is("food for days")),
							hasProperty("category", is(Category.PIZZA))
							))));
		
		verify(restaurantService, times(1)).findByNameIgnoreCaseContaining(filter);
		verifyNoMoreInteractions(restaurantService);

	}
	
	@Test
	public void searchByName_ShouldSearchWithFilledFilter() throws Exception {

		RestaurantFilter filter = new RestaurantFilter();
		filter.setName("Abra");

		Restaurant first = new RestaurantBuilder()
				.id(1L)
				.name("Abra")
				.description("lots of food")
				.category(Category.ITALIAN).build();

		when(restaurantService.findByNameIgnoreCaseContaining(filter)).thenReturn(Arrays.asList(first));
		
		System.out.println(restaurantService.findByNameIgnoreCaseContaining(filter));

		this.mockMvc.perform(get("/restaurants?name=Abra"))
			.andExpect(status().isOk())
			.andExpect(view().name("restaurantList"))
			.andExpect(model().attribute("restaurants", hasSize(1)))
			.andExpect(model().attribute("restaurants",
					hasItem(allOf(
							hasProperty("id", is(1L)),
							hasProperty("name", is("Abra")),
							hasProperty("description", is("lots of food")),
							hasProperty("category", is(Category.ITALIAN))
							))));
		
		verify(restaurantService, times(2)).findByNameIgnoreCaseContaining(filter);
		verifyNoMoreInteractions(restaurantService);

	}
	
	@Test
	public void newRestaurant_ShouldRenderNewRestaurantFormView() throws Exception {

		 mockMvc.perform(get("/restaurants/new"))
         .andExpect(status().isOk())
         .andExpect(view().name("restaurantAdd"));

	}
	
	@Test
    public void add_NameAndDescriptionAreTooLong_ShouldRenderFormViewAndReturnValidationErrorsForNameAndDescription() throws Exception {
        
		String name = TestUtil.createStringWithLength(101);
        String description = TestUtil.createStringWithLength(501);
 
        mockMvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", name)
                .param("description", description)
                .sessionAttr("restaurant", new Restaurant())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("restaurantAdd"))
                .andExpect(model().attributeHasFieldErrors("restaurant", "name"))
                .andExpect(model().attributeHasFieldErrors("restaurant", "description"))
                .andExpect(model().attribute("restaurant", hasProperty("id", nullValue())))
                .andExpect(model().attribute("restaurant", hasProperty("name", is(name))))
                .andExpect(model().attribute("restaurant", hasProperty("description", is(description))));
                
 
        verifyZeroInteractions(restaurantService);
    }
	
	@Test
    public void edit_ShouldAddRestaurantEntryAndRenderViewRestaurantEntryView() throws Exception {
        
		Restaurant unedited = new RestaurantBuilder()
				.id(1L)
				.name("Abra")
				.description("lots of food")
				.category(Category.VEGETARIAN).build();
		
		Restaurant edited = new RestaurantBuilder()
				.id(1L)
				.name("Peppo")
				.description("italian food")
				.category(Category.ITALIAN).build();
        
        when(restaurantService.save(unedited)).thenReturn(edited);
 
        mockMvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr("restaurant", unedited)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("restaurantAdd"));
                
                
 
        verifyZeroInteractions(restaurantService);
    }
	
	@SuppressWarnings("deprecation")
	@Test
    public void add_NewRestaurantEntry_ShouldAddRestaurantEntryAndRenderViewRestaurantEntryView() throws Exception {
		Restaurant added = new RestaurantBuilder()
				.id(1L)
				.name("name")
				.description("description")
				.category(Category.ITALIAN).build();
 
        when(restaurantService.save(isA(Restaurant.class))).thenReturn(added);
 
        mockMvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "name")
                .param("description", "description")
                .sessionAttr("restaurant", new Restaurant())
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name("redirect:/restaurants/new"))
                .andExpect(redirectedUrl("/restaurants/new"))
                .andExpect(flash().attribute("message", is("Restaurante salvo com sucesso!")));
 
        ArgumentCaptor<Restaurant> formObjectArgument = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantService, times(1)).save(formObjectArgument.capture());
        verifyNoMoreInteractions(restaurantService);
 
        Restaurant formObject = formObjectArgument.getValue();
 
        assertThat(formObject.getName(), is("name"));
        assertThat(formObject.getDescription(), is("description"));
        assertNull(formObject.getId());
        
    }

}
