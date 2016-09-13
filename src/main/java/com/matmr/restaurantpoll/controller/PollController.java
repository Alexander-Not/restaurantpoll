package com.matmr.restaurantpoll.controller;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.service.PollService;
import com.matmr.restaurantpoll.service.RestaurantService;
import com.matmr.restaurantpoll.validator.VoteValidator;

@Controller
@RequestMapping("/poll")
public class PollController {

	@Autowired
	private PollService pollService;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	VoteValidator voteValidator;

	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("votes", pollService.getVotes(LocalDate.now()));

		return "votes";
	}

	@RequestMapping(value = "/vote", method = RequestMethod.GET)
	public String voteForm(Model model) {
		Vote vote = new Vote();
		List<Restaurant> restaurants = restaurantService.getAllRestaurants();
		
		model.addAttribute("vote", vote);
		model.addAttribute("restaurants", pollService.availableToVote(restaurants));
		model.addAttribute("lastWinners", pollService.getLastWeekWinners(restaurants));
		return "poll";
	}

	@RequestMapping(value = "/vote", method = RequestMethod.POST)
	public String processVoteForm(@ModelAttribute("vote") Vote vote, Errors errors, BindingResult result,
			RedirectAttributes attribute, Model model) {

		List<Restaurant> restaurants = restaurantService.getAllRestaurants();
		addUserAndDateTo(vote);
		voteValidator.validate(vote, result);

		if (errors.hasErrors()) {
			model.addAttribute("restaurants", pollService.availableToVote(restaurants));
			return "poll";
		}

		pollService.vote(vote);
		return "redirect:/poll/results";

	}

	@RequestMapping("/results")
	public String result(Model model) {
		
		LocalDate today = LocalDate.now();

		model.addAttribute("winner", pollService.getWinner(today, restaurantService.getAllRestaurants()));
		model.addAttribute("votes", pollService.getVotes(today));

		return "result";
	}

	private void addUserAndDateTo(Vote vote) {
		vote.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		vote.setDate(LocalDate.now());
	}

}
