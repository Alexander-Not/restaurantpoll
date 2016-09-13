package com.matmr.restaurantpoll.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.matmr.restaurantpoll.exception.RestaurantNotFoundException;
import com.matmr.restaurantpoll.model.Category;
import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.filter.RestaurantFilter;
import com.matmr.restaurantpoll.repository.RestaurantRepository;
import com.matmr.restaurantpoll.util.RestaurantBuilder;

public class RestaurantServiceTest {

	public static final Long ID = 1L;
	public static final String NAME = "name";
	public static final String UPDATED_NAME = "updated name";
	public static final String DESCRIPTION = "description";
	public static final String UPDATED_DESCRIPTION = "updated description";
	public static final Category CATEGORY = Category.BARBECUE;
	public static final Category UPDATED_CATEGORY = Category.PIZZA;

	private RestaurantRepository restaurantRepositoryMock;

	private RestaurantService restaurantService;

	@Before
	public void setUp() {
		restaurantRepositoryMock = mock(RestaurantRepository.class);
		restaurantService = new RestaurantService(restaurantRepositoryMock);
	}

	@Test
	public void getAllRestaurants_ShouldReturnListOfRestaurantEntries() {

		List<Restaurant> models = new ArrayList<>();
		when(restaurantRepositoryMock.findAll()).thenReturn(models);

		List<Restaurant> actual = restaurantService.getAllRestaurants();

		verify(restaurantRepositoryMock, times(1)).findAll();
		verifyNoMoreInteractions(restaurantRepositoryMock);

		assertThat(actual, is(models));
	}
	
	@Test
	public void save_ShouldSaveRestaurant() {

		Restaurant restaurant = new RestaurantBuilder().id(ID).name(NAME).description(DESCRIPTION).category(CATEGORY)
				.build();

		restaurantService.save(restaurant);

		ArgumentCaptor<Restaurant> restaurantArgument = ArgumentCaptor.forClass(Restaurant.class);
		verify(restaurantRepositoryMock, times(1)).save(restaurantArgument.capture());
		verifyNoMoreInteractions(restaurantRepositoryMock);

		Restaurant model = restaurantArgument.getValue();

		assertEquals(NAME, model.getName());
	}

	@Test
	public void findByNameIgnoreCaseContaining_RestaurantEntryFound_ShouldReturnFoundRestaurantEntry() {

		RestaurantFilter filter = new RestaurantFilter();
		filter.setName("chu");

		Restaurant model = new RestaurantBuilder().id(ID).name(NAME).description(DESCRIPTION).category(CATEGORY)
				.build();

		when(restaurantRepositoryMock.findByNameIgnoreCaseContaining("chu")).thenReturn(Arrays.asList(model));

		List<Restaurant> actual = restaurantService.findByNameIgnoreCaseContaining(filter);

		verify(restaurantRepositoryMock, times(1)).findByNameIgnoreCaseContaining("chu");
		verifyNoMoreInteractions(restaurantRepositoryMock);

		assertEquals(actual, Arrays.asList(model));
		assertEquals(1, actual.size());

	}

	@Test(expected = RestaurantNotFoundException.class)
	public void findById_RestaurantEntryNotFound_ShouldThrowException() throws RestaurantNotFoundException {
		when(restaurantRepositoryMock.findOne(ID)).thenReturn(null);

		restaurantService.findById(ID);

		verify(restaurantRepositoryMock, times(1)).findOne(ID);
		verifyNoMoreInteractions(restaurantRepositoryMock);
	}

	@Test
	public void findById_RestaurantEntryFound_ShouldReturnFoundRestaurantEntry() throws RestaurantNotFoundException {

		Restaurant model = new RestaurantBuilder().id(ID).name(NAME).description(DESCRIPTION).category(CATEGORY)
				.build();

		when(restaurantRepositoryMock.findOne(ID)).thenReturn(model);

		Restaurant actual = restaurantService.findById(ID);

		verify(restaurantRepositoryMock, times(1)).findOne(ID);
		verifyNoMoreInteractions(restaurantRepositoryMock);

		assertThat(actual, is(model));
	}

	

	@Test
	public void delete_RestaurantEntryFound_ShouldDeleteRestaurantEntryAndReturnIt()
			throws RestaurantNotFoundException {

		Restaurant model = new RestaurantBuilder().id(ID).name(NAME).description(DESCRIPTION).category(CATEGORY)
				.build();

		when(restaurantRepositoryMock.findOne(ID)).thenReturn(model);

		Restaurant actual = restaurantService.deleteById(ID);

		verify(restaurantRepositoryMock, times(1)).findOne(ID);
		verify(restaurantRepositoryMock, times(1)).delete(model);
		verifyNoMoreInteractions(restaurantRepositoryMock);

		assertThat(actual, is(model));
	}
	
//	@Test
//	public void availableToVote_ShouldReturnListOfRestauratsThatDidNotWinInTheLastFiveDays() {
//		
//		Restaurant r1 = new RestaurantBuilder()
//				.id(ID)
//				.name(NAME)
//				.description(DESCRIPTION)
//				.category(CATEGORY).build();
//		Restaurant r2 = new RestaurantBuilder()
//				.id(ID + 1)
//				.name(NAME)
//				.description(DESCRIPTION)
//				.category(CATEGORY).build();
//		Restaurant r3 = new RestaurantBuilder()
//				.id(ID + 2).name(NAME)
//				.description(DESCRIPTION)
//				.category(CATEGORY).build();
//		
//		when(restaurantRepositoryMock.findAll()).thenReturn(Arrays.asList(r1,r2,r3));
//		
//		List<Restaurant> actualList = restaurantService.availableToVote();
//		
//		verify(restaurantRepositoryMock, times(1)).findAll();
//		verifyNoMoreInteractions(restaurantRepositoryMock);
//
//		assertThat(actualList, is(Arrays.asList(r1,r2,r3)));
//	}

}
