package com.matmr.restaurantpoll.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.repository.VoteRepository;

@Service
public class VoteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

	@Autowired
	private VoteRepository voteRepository;

	public List<Vote> filterByDate(LocalDate date) {

		LOGGER.debug("Finding vote entries to date: {}", date);

		List<Vote> filteredList = new ArrayList<Vote>();

		for (Vote vote : voteRepository.findAll()) {
			if (date.equals(vote.getDate())) {
				filteredList.add(vote);
			}
		}

		return filteredList;
	}

	public List<Vote> getVotesByRestaurantId(String restaurantId) {

		List<Vote> votesByRestaurant = new ArrayList<Vote>();

		LOGGER.debug("Finding votes to restaurant with id: {}", restaurantId);

		for (Vote vote : voteRepository.findAll()) {
			if (restaurantId.equals(vote.getChosenRestaurantId())) {
				votesByRestaurant.add(vote);
			}
		}
		return votesByRestaurant;

	}

	public List<Vote> findAll() {
		LOGGER.debug("Finding all vote entries");
		return voteRepository.findAll();
	}

}
