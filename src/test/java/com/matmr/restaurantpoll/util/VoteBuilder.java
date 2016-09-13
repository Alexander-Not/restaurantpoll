package com.matmr.restaurantpoll.util;

import org.joda.time.LocalDate;

import com.matmr.restaurantpoll.model.Vote;

public class VoteBuilder {

	Vote vote;

	public VoteBuilder() {
		vote = new Vote();
	}

	public VoteBuilder id(Long id) {
		vote.setId(id);
		return this;
	}

	public VoteBuilder date(LocalDate date) {
		vote.setDate(date);
		return this;
	}

	public VoteBuilder chosenRestaurantId(Long chosenRestaurantId) {
		vote.setChosenRestaurantId(chosenRestaurantId);
		return this;
	}

	public VoteBuilder username(String username) {
		vote.setUsername(username);
		return this;
	}

	public Vote build() {
		return vote;
	}

}
