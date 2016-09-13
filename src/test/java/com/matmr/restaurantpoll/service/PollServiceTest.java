package com.matmr.restaurantpoll.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.matmr.restaurantpoll.model.Category;
import com.matmr.restaurantpoll.model.Poll;
import com.matmr.restaurantpoll.model.Restaurant;
import com.matmr.restaurantpoll.model.Vote;
import com.matmr.restaurantpoll.repository.PollRepository;
import com.matmr.restaurantpoll.util.RestaurantBuilder;
import com.matmr.restaurantpoll.util.VoteBuilder;

public class PollServiceTest {

	public static final Long ID = 1L;
	public static final Long CHOSEN_RESTAURANT_ID = 1L;
	public static final String USERNAME = "name";
	public static final LocalDate DATE = new LocalDate(1983, 12, 22);
	
	public static final String RESTAURANT_NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final Category CATEGORY = Category.URUGUAIAN;

	private PollRepository pollRepositoryMock;

	private PollService pollService;

	@Before
	public void setUp() {
		pollRepositoryMock = mock(PollRepository.class);
		pollService = new PollService(pollRepositoryMock);
	}

	@Test
	public void getPollByDate_ShouldCreatePollForNonPersistedPollDay() {

		Poll created = new Poll(DATE);
		when(pollRepositoryMock.getOrCreatePoll(DATE)).thenReturn(created);

		Poll actual = pollService.getPollByDate(DATE);

		verify(pollRepositoryMock, times(1)).getOrCreatePoll(DATE);
		verifyNoMoreInteractions(pollRepositoryMock);

		assertThat(actual, is(created));
	}

	@Test
	public void getOrCreatePoll_ShouldCreatePollAndThenFindCorrespondingPoll() {

		Poll origin = new Poll(DATE);

		when(pollRepositoryMock.getOrCreatePoll(DATE)).thenReturn(origin);

		Poll created = pollService.getPollByDate(DATE);
		Poll found = pollService.getPollByDate(DATE);

		verify(pollRepositoryMock, times(2)).getOrCreatePoll(DATE);
		verifyNoMoreInteractions(pollRepositoryMock);

		assertThat(origin, is(created));
		assertThat(origin, is(found));
		assertThat(found, is(created));
	}

	@Test
	public void vote_ShouldAddVoteToRepository() {

		Vote added = new VoteBuilder().id(ID).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();

		when(pollRepositoryMock.addVote(added)).thenReturn(added);

		Vote actual = pollService.vote(added);

		verify(pollRepositoryMock, times(1)).addVote(added);
		verifyNoMoreInteractions(pollRepositoryMock);

		assertThat(added, is(actual));

	}

	@Test
	public void getVotes_ShouldReturnListOfVotesToGivenDate() {

		Vote added1 = new VoteBuilder().id(ID).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();
		Vote added2 = new VoteBuilder().id(ID + 1).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID)
				.username(USERNAME).build();

		when(pollRepositoryMock.getVotesByDate(DATE)).thenReturn(Arrays.asList(added1, added2));

		List<Vote> actual = pollService.getVotes(DATE);

		verify(pollRepositoryMock, times(1)).getVotesByDate(DATE);
		verifyNoMoreInteractions(pollRepositoryMock);

		assertThat(Arrays.asList(added1, added2), is(actual));

	}
	
	@Test
	public void availableToVote_ShouldReturnListOfRestauratsThatDidNotWinInTheLastFiveDays() {
		
		Restaurant r1 = new RestaurantBuilder()
				.id(ID)
				.name(RESTAURANT_NAME)
				.description(DESCRIPTION)
				.category(CATEGORY).build();
		Restaurant r2 = new RestaurantBuilder()
				.id(ID + 1)
				.name(RESTAURANT_NAME+"A")
				.description(DESCRIPTION)
				.category(CATEGORY).build();
		Restaurant r3 = new RestaurantBuilder()
				.id(ID + 2).name(RESTAURANT_NAME+"B")
				.description(DESCRIPTION)
				.category(CATEGORY).build();
		
		Vote vote = new VoteBuilder().chosenRestaurantId(ID).date(DATE.minusDays(1)).build();
		
		when(pollRepositoryMock.getVotesByDate(LocalDate.now().minusDays(1))).thenReturn(Arrays.asList(vote));
		
		List<Restaurant> actualList = pollService.availableToVote(Arrays.asList(r1,r2,r3));
		
		verify(pollRepositoryMock, times(1)).getVotesByDate(LocalDate.now().minusDays(1));
		assertThat(actualList, is(Arrays.asList(r2,r3)));
	}

	@Test
	public void getWinner_ShouldReturnRestaurantWithMoreVotes() {

		Vote v1 = new VoteBuilder().id(ID).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();
		Vote v2 = new VoteBuilder().id(ID + 1).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();
		Vote v3 = new VoteBuilder().id(ID).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();

		Restaurant twoVotesTotal = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID).build();
		Restaurant oneVotesTotal = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID + 1).build();
		Restaurant zeroVoteTotal = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID + 2).build();

		when(pollRepositoryMock.getVotesByDate(DATE)).thenReturn(Arrays.asList(v1, v2, v3));

		Restaurant winner = pollService.getWinner(DATE, Arrays.asList(oneVotesTotal, twoVotesTotal, zeroVoteTotal));

		verify(pollRepositoryMock, times(1)).getVotesByDate(DATE);
		verifyNoMoreInteractions(pollRepositoryMock);
		assertThat(CHOSEN_RESTAURANT_ID, is(winner.getId()));

	}
	
	@Test
	public void userHasVoted_ShouldReturnTrueBecauseUserAlreadyVotedInTheDate() {
		
		Vote vote = new VoteBuilder().id(ID).date(DATE).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();
		
		when(pollRepositoryMock.getVotesByDate(DATE)).thenReturn(Arrays.asList(vote));

		Boolean hasVoted = pollService.userHasVoted(USERNAME, DATE);
		
		verify(pollRepositoryMock, times(1)).getVotesByDate(DATE);
		verifyNoMoreInteractions(pollRepositoryMock);
		assertThat(hasVoted, is(true));
		
	}
	
	@Test
	public void userHasVoted_ShouldReturnFalseBecauseUserAlreadyDidNotVoteThatDate() {
		
		Vote vote = new VoteBuilder().id(ID).date(DATE.minusDays(1)).chosenRestaurantId(CHOSEN_RESTAURANT_ID).username(USERNAME)
				.build();
		
		when(pollRepositoryMock.getVotesByDate(DATE)).thenReturn(Arrays.asList(vote));

		Boolean hasVoted = pollService.userHasVoted(USERNAME, DATE);
		
		verify(pollRepositoryMock, times(1)).getVotesByDate(DATE);
		verifyNoMoreInteractions(pollRepositoryMock);
		assertThat(hasVoted, is(true));
		
	}

	@Test
	public void getLastWeekWinners_ShouldReturnListOfThreeRestauratsThatWonInTheLastFiveDays() {

		Restaurant r1 = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID).build();
		Restaurant r2 = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID + 1).build();
		Restaurant r3 = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID + 2).build();
		Restaurant r4 = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID + 3).build();
		Restaurant r5 = new RestaurantBuilder().id(CHOSEN_RESTAURANT_ID + 4).build();
		List<Restaurant> candidates = Arrays.asList(r1, r2, r3, r4, r5);

		Vote v1 = new VoteBuilder().id(ID)
				.date(LocalDate.now().minusDays(1))
				.chosenRestaurantId(CHOSEN_RESTAURANT_ID).build();
		
		Vote v2 = new VoteBuilder().id(ID + 1)
				.date(LocalDate.now().minusDays(2))
				.chosenRestaurantId(CHOSEN_RESTAURANT_ID + 1).build();
		
		Vote v3 = new VoteBuilder().id(ID + 2)
				.date(LocalDate.now().minusDays(3))
				.chosenRestaurantId(CHOSEN_RESTAURANT_ID + 2).build();
		
		Vote v4 = new VoteBuilder().id(ID + 3)
				.date(LocalDate.now().minusDays(3))
				.chosenRestaurantId(CHOSEN_RESTAURANT_ID + 3).build();
		
		Vote v5 = new VoteBuilder().id(ID + 4)
				.date(LocalDate.now().minusDays(3))
				.chosenRestaurantId(CHOSEN_RESTAURANT_ID + 4).build();

		when(pollRepositoryMock.getVotesByDate(LocalDate.now().minusDays(1))).thenReturn(Arrays.asList(v1));
		when(pollRepositoryMock.getVotesByDate(LocalDate.now().minusDays(2))).thenReturn(Arrays.asList(v2));
		when(pollRepositoryMock.getVotesByDate(LocalDate.now().minusDays(3))).thenReturn(Arrays.asList(v3));
		when(pollRepositoryMock.getVotesByDate(LocalDate.now().minusDays(4))).thenReturn(Arrays.asList(v4));
		when(pollRepositoryMock.getVotesByDate(LocalDate.now().minusDays(5))).thenReturn(Arrays.asList(v5));

		List<Restaurant> actualList = pollService.getLastWeekWinners(candidates);

		verify(pollRepositoryMock, times(1)).getVotesByDate(LocalDate.now().minusDays(1));
		verify(pollRepositoryMock, times(1)).getVotesByDate(LocalDate.now().minusDays(2));
		verify(pollRepositoryMock, times(1)).getVotesByDate(LocalDate.now().minusDays(3));
		verify(pollRepositoryMock, times(1)).getVotesByDate(LocalDate.now().minusDays(4));
		verify(pollRepositoryMock, times(1)).getVotesByDate(LocalDate.now().minusDays(5));
		
		verifyNoMoreInteractions(pollRepositoryMock);
		assertThat(actualList, is(Arrays.asList(r1, r2, r3, r4, r5)));
	}

}
