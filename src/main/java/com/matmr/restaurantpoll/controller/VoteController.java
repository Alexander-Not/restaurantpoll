package com.matmr.restaurantpoll.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.service.VoteService;

@Controller
@RequestMapping("/votes")
public class VoteController {

	@Autowired
	private VoteService voteService;

	@Autowired
	public VoteController(VoteService voteService) {
		this.voteService = voteService;
	}

	@RequestMapping
	public ModelAndView list() {

		List<Vote> votes = voteService.findAll();
		ModelAndView mv = new ModelAndView("votes");
		mv.addObject("votes", votes);

		return mv;
	}

}