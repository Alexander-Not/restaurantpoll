package com.matmr.restaurantpoll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matmr.restaurantpoll.model.Restaurant;
	
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

	List<Restaurant> findByNameIgnoreCaseContaining(String filter);
	
}
