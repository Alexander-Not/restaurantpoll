package com.matmr.restaurantpoll.exception;

public class RestaurantNotFoundException extends Exception {

	private static final long serialVersionUID = -2317787168812490650L;

	public RestaurantNotFoundException(String message) {
		super(message);
	}

}
