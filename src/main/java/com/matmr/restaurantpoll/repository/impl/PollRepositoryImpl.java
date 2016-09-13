package com.matmr.restaurantpoll.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.matmr.restaurantpoll.model.Poll;
import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.repository.PollRepositoryCustom;

@Repository
public class PollRepositoryImpl implements PollRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(PollRepositoryImpl.class);

	private List<Poll> listOfPolls = new ArrayList<Poll>();

	@Override
	public Poll getOrCreatePoll(LocalDate date) {

		Poll existingPoll = null;

		for (Poll poll : listOfPolls) {
			if (poll != null && poll.getDate() != null && poll.getDate().equals(date)) {
				existingPoll = poll;
				LOGGER.debug("Loading an existing poll: {}", existingPoll);
				break;
			}
		}

		if (existingPoll == null) {
			existingPoll = new Poll(LocalDate.now());
			listOfPolls.add(existingPoll);
			LOGGER.debug("Creating a new poll: {}", existingPoll);
		}
		return existingPoll;
	}

	@Override
	public Vote addVote(Vote vote) {
		LOGGER.debug("Adding vote to poll: {}", vote);
		Poll poll = getOrCreatePoll(vote.getDate());
		poll.getVotes().add(vote);
		return vote;
	}

	@Override
	public List<Vote> getVotesByDate(LocalDate date) {
		LOGGER.debug("Getting all votes to date: {}", date);
		return getOrCreatePoll(date).getVotes();
	}

}
