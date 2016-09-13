package com.matmr.restaurantpoll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matmr.restaurantpoll.exception.RestaurantNotFoundException;
import com.matmr.restaurantpoll.model.Category;
import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.filter.RestaurantFilter;
import com.matmr.restaurantpoll.repository.RestaurantRepository;
import com.matmr.restaurantpoll.util.RestaurantBuilder;

@Service
public class RestaurantService {

	@Autowired
	RestaurantRepository restaurantRepository;

	public RestaurantService() {

	}

	public RestaurantService(RestaurantRepository restaurantRepository) {

		this.restaurantRepository = restaurantRepository;
	}

	public List<Restaurant> getAllRestaurants() {

		return restaurantRepository.findAll();
	}

	public Restaurant save(Restaurant restaurant) {

		restaurantRepository.save(restaurant);
		return restaurant;
	}

	public List<Restaurant> findByNameIgnoreCaseContaining(RestaurantFilter filter) {

		String name = filter.getName() == null ? "%" : filter.getName();
		return restaurantRepository.findByNameIgnoreCaseContaining(name);
	}

	@Transactional(rollbackFor = { RestaurantNotFoundException.class })
	public Restaurant deleteById(Long id) throws RestaurantNotFoundException {

		Restaurant deleted = findById(id);
		restaurantRepository.delete(deleted);
		return deleted;

	}

	@Transactional(readOnly = true, rollbackFor = { RestaurantNotFoundException.class })
	public Restaurant findById(Long id) throws RestaurantNotFoundException {

		Restaurant found = restaurantRepository.findOne(id);

		if (found == null) {
			throw new RestaurantNotFoundException("No restaurant found with id: " + id);
		}

		return found;

	}
	//
//	 public List<Restaurant> availableToVote() {
//	 LOGGER.debug("Getting all restaurants available");
//	
//	 List<Restaurant> avaiableRestaurants = new
//	 ArrayList<Restaurant>(getAllRestaurants());
//	 avaiableRestaurants.removeAll(getLastWeekWinners());
//	
//	 return avaiableRestaurants;
//	 }
	//
	// public List<Restaurant> getLastWeekWinners() {
	// LOGGER.debug("Geting winners of the last week: {}");
	//
	// List<Restaurant> fiveLastWinners = new ArrayList<Restaurant>();
	//
	// for (int i = 1; i <= 5; i++) {
	// Restaurant winner = pollService.getWinner(LocalDate.now().minusDays(i),
	// getAllRestaurants());
	// if (winner != null) {
	// fiveLastWinners.add(winner);
	// }
	// }
	//
	// return fiveLastWinners;
	// }

	public void generateRestaurants() {

		Restaurant r1 = new RestaurantBuilder().name("Peppo Cucina").description("SALMONE ALLE FRAGOLE!")
				.category(Category.ITALIAN).build();

		Restaurant r2 = new RestaurantBuilder().name("Fornelonne").description("Rodízio de pizzas")
				.category(Category.PIZZA).build();

		Restaurant r3 = new RestaurantBuilder().name("Kung Food").description("Comida chinesa")
				.category(Category.CHINESE).build();

		Restaurant r4 = new RestaurantBuilder().name("Raw Fish").description("Sushi, sashimi e mais")
				.category(Category.JAPANESE).build();

		Restaurant r5 = new RestaurantBuilder().name("Churrascaria CTG").description("Meat!")
				.category(Category.BARBECUE).build();

		Restaurant r6 = new RestaurantBuilder().name("Ojo del Diablo")
				.description("carne de cordeiro e coisas caóticas").category(Category.URUGUAIAN).build();

		Restaurant r7 = new RestaurantBuilder().name("Ateliê das massas").description("pasta italiana amore mio")
				.category(Category.ITALIAN).build();

		Restaurant r8 = new RestaurantBuilder().name("Kung Food").description("Comida chinesa")
				.category(Category.ITALIAN).build();

		Restaurant r9 = new RestaurantBuilder().name("Prato Verde")
				.description("Comida ruim, tirando as pizzas da sexta").category(Category.VEGETARIAN).build();

		Restaurant r10 = new RestaurantBuilder().name("Govinda").description("xis").category(Category.VEGETARIAN)
				.build();

		save(r1);
		save(r2);
		save(r3);
		save(r4);
		save(r5);
		save(r6);
		save(r7);
		save(r8);
		save(r9);
		save(r10);

	}

}
