package com.matmr.restaurantpoll.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matmr.restaurantpoll.model.Poll;
import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.repository.PollRepository;

@Service
public class PollService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PollService.class);

	@Autowired
	PollRepository pollRepository;

	@Autowired
	RestaurantService restaurantService;

	public PollService() {
	}

	public PollService(PollRepository pollRepository) {
		this.pollRepository = pollRepository;
	}

	public Poll getPollByDate(LocalDate date) {
		LOGGER.debug("Getting poll with date: {}", date);
		return pollRepository.getOrCreatePoll(date);
	}

	public Vote vote(Vote vote) {
		LOGGER.debug("Adding vote: {}", vote);
		pollRepository.addVote(vote);
		return vote;
	}

	public List<Vote> getVotes(LocalDate date) {
		LOGGER.debug("Finding all vote entries to date: : {}", date);
		return pollRepository.getVotesByDate(date);
	}

	public Restaurant getWinner(LocalDate date, List<Restaurant> restaurants) {
		LOGGER.debug("Finding winner to date: {}", date);
		List<Vote> votes = pollRepository.getVotesByDate(date);
		Restaurant winner = findWinner(date, restaurants, votes);
		return winner;
	}

	public Boolean userHasVoted(String name, LocalDate date) {
		LOGGER.debug("Checking if user is able to vote: {}", name);
		List<Vote> votes = pollRepository.getVotesByDate(date);
		for (Vote vote : votes) {
			if (vote.getUsername().equals(name)) {
				return true;
			}
		}
		return false;

	}
	
	public List<Restaurant> availableToVote(List<Restaurant> restaurants) {
		 LOGGER.debug("Getting all restaurants available");
		
		 List<Restaurant> avaiableRestaurants = new ArrayList<Restaurant>(restaurants);
		 avaiableRestaurants.removeAll(getLastWeekWinners(restaurants));
		
		 return avaiableRestaurants;
		 }

	private Restaurant findWinner(LocalDate date, List<Restaurant> restaurants, List<Vote> votes) {
		LOGGER.debug("Checking winner to date: {}", date);
		Restaurant winner = null;
		Long mostVotes = 0L;

		for (Restaurant restaurant : restaurants) {
			
			Boolean restaurantHasMoreVotes = votes.stream()
					.filter(c -> restaurant.getId().equals(c.getChosenRestaurantId())).count() > mostVotes;

			if (restaurantHasMoreVotes) {
				winner = restaurant;
				mostVotes = votes.stream().filter(c -> restaurant.getId().equals(c.getChosenRestaurantId()))
						.count();
			}
		}
		return winner; 
	}
	
	public List<Restaurant> getLastWeekWinners(List<Restaurant> candidates) {
		LOGGER.debug("Geting winners of the last week");

		List<Restaurant> fiveLastWinners = new ArrayList<Restaurant>();
		//List<Restaurant> candidates = restaurantService.getAllRestaurants();

		for (int day = 1; day <= 5; day++) {
			LocalDate date = LocalDate.now().minusDays(day);
			List<Vote> votes = pollRepository.getVotesByDate(LocalDate.now().minusDays(day));
			
			Restaurant winner = findWinner(date, candidates, votes);
			
			if (winner != null) {
				fiveLastWinners.add(winner);
			}
		}

		return fiveLastWinners;
	}

}
