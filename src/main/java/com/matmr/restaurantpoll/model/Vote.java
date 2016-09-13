package com.matmr.restaurantpoll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.joda.time.LocalDate;

@Entity
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	private Long chosenRestaurantId;

	private String username;
	
	private String restaurantName; 

	public Vote() {	}

	public Vote(LocalDate date, Long chosenRestaurantId, String username) {
		this.date = date;
		this.chosenRestaurantId = chosenRestaurantId;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getChosenRestaurantId() {
		return chosenRestaurantId;
	}

	public void setChosenRestaurantId(Long chosenRestaurantId) {
		this.chosenRestaurantId = chosenRestaurantId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chosenRestaurantId == null) ? 0 : chosenRestaurantId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vote other = (Vote) obj;
		if (chosenRestaurantId == null) {
			if (other.chosenRestaurantId != null)
				return false;
		} else if (!chosenRestaurantId.equals(other.chosenRestaurantId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vote [chosenRestaurantId=" + chosenRestaurantId + ", username=" + username + ", date=" + date + "]";
	}

}
