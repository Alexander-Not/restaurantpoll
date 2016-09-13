package com.matmr.restaurantpoll.repository;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.matmr.restaurantpoll.model.Poll;
import com.matmr.restaurantpoll.model.Vote;

@Repository
public interface PollRepositoryCustom {

	public Poll getOrCreatePoll(LocalDate date);
	
	public Vote addVote(Vote vote);
	
	public List<Vote> getVotesByDate(LocalDate date);
}
