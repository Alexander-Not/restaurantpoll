package com.matmr.restaurantpoll.util;

import com.matmr.restaurantpoll.model.Category;
import com.matmr.restaurantpoll.model.Restaurant;

public class RestaurantBuilder {

	Restaurant restaurant;

	public RestaurantBuilder() {
		restaurant = new Restaurant();
	}

	public RestaurantBuilder id(Long id) {
		restaurant.setId(id);
		return this;
	}

	public RestaurantBuilder name(String name) {
		restaurant.setName(name);
		return this;
	}

	public RestaurantBuilder description(String description) {
		restaurant.setDescription(description);
		return this;
	}

	public RestaurantBuilder category(Category category) {
		restaurant.setCategory(category);
		return this;
	}

	public Restaurant build() {
		return restaurant;
	}

}
