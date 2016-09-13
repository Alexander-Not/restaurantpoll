package com.matmr.restaurantpoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matmr.restaurantpoll.model.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>, PollRepositoryCustom {

}
