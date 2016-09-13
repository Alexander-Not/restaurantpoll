package com.matmr.restaurantpoll.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.matmr.restaurantpoll.exception.RestaurantNotFoundException;
import com.matmr.restaurantpoll.model.Category;
import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.filter.RestaurantFilter;
import com.matmr.restaurantpoll.service.RestaurantService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	public RestaurantController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@RequestMapping("/generate")
	public String generateRestaurants(RedirectAttributes attributes) {

		restaurantService.generateRestaurants();
		attributes.addFlashAttribute("message", "Restaurantes gerados!");

		return "redirect:/index";
	}

	@RequestMapping("/new")
	public ModelAndView newRestaurant() {
		ModelAndView mv = new ModelAndView("restaurantAdd");
		mv.addObject(new Restaurant());
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(@Validated Restaurant restaurant, Errors errors, RedirectAttributes attributes) {

		if (errors.hasErrors()) {
			return "restaurantAdd";
		}

		restaurantService.save(restaurant);
		attributes.addFlashAttribute("message", "Restaurante salvo com sucesso!");
		return "redirect:/restaurants/new";
	}

	@RequestMapping
	public ModelAndView searchByName(@ModelAttribute("filter") RestaurantFilter filter) {

		List<Restaurant> filteredRestaurants = restaurantService.findByNameIgnoreCaseContaining(filter);
		ModelAndView mv = new ModelAndView("restaurantList");
		mv.addObject("restaurants", filteredRestaurants);

		return mv;
	}

	@RequestMapping("{id}")
	public ModelAndView edit(@PathVariable("id") Restaurant restaurant) {
		ModelAndView mv = new ModelAndView("restaurantAdd");
		mv.addObject(restaurant);
		return mv;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long id, RedirectAttributes attributes) throws RestaurantNotFoundException {
		restaurantService.deleteById(id);

		attributes.addFlashAttribute("message", "Restaurante excluído com sucesso!");
		return "redirect:/restaurants";
	}

	@ModelAttribute("allCategories")
	public List<Category> allCategories() {
		return Arrays.asList(Category.values());
	}

}