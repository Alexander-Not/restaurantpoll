package com.matmr.restaurantpoll.model;

public enum Category {

	ITALIAN("Italiano"), BARBECUE("Churrascaria"), VEGETARIAN("Vegetariano"), PIZZA("Pizzaria"), URUGUAIAN(
			"Uruguaio"), JAPANESE("Japonês"), CHINESE("Chinês");

	private String description;

	private Category(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
