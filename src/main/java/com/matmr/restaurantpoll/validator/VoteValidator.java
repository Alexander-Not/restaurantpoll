package com.matmr.restaurantpoll.validator;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.service.PollService;

@Component
public class VoteValidator implements Validator {

	@Autowired
	PollService pollService;

	public boolean supports(Class<?> type) {
		return type == Vote.class;
	}

	public void validate(Object target, Errors errors) {
		
		Vote vote = (Vote) target;
		String username = vote.getUsername();
		LocalDate date = vote.getDate();
		Long id = vote.getChosenRestaurantId();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.vote.empty");

		if (hasVoted(username, date)) {
			errors.reject("user.already.voted", "Você já votou hoje, " + username);
		}
		
		if (id == null) {
			errors.reject("user.did.not.voted", "Escolha um restaurante!");
		}
	}

	private boolean hasVoted(String username, LocalDate date) {
		return pollService.userHasVoted(username, date);
	}

}