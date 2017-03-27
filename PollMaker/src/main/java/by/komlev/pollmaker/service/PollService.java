package by.komlev.pollmaker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import by.komlev.pollmaker.exception.ResourceNotFoundException;
import by.komlev.pollmaker.exception.ResourceOperationConflictException;
import by.komlev.pollmaker.exception.ResourceValidationException;
import by.komlev.pollmaker.model.Poll;
import by.komlev.pollmaker.model.PollStatus;
import by.komlev.pollmaker.model.Vote;

@Service
public class PollService {

	private static final AtomicInteger pollIdGenerator = new AtomicInteger();

	private static final AtomicInteger answerIdGenerator = new AtomicInteger();

	private static final AtomicInteger voteIdGenerator = new AtomicInteger();

	private static List<Poll> polls = new ArrayList<>();

	private void validatePoll(Poll poll) throws ResourceValidationException {

		if (poll.getTheme().isEmpty()) {

			throw new ResourceValidationException("poll theme must not be empty");
		}

		if (poll.getAnswers().size() < 2) {

			throw new ResourceValidationException("poll must have at least two answers");
		}
	}

	public Poll getPoll(int pollId) throws ResourceNotFoundException {

		Poll result = polls.stream().filter(poll -> poll.getId() == pollId).findFirst().orElse(null);

		if (result == null) {

			throw new ResourceNotFoundException();
		}

		return result;
	}

	public void savePoll(Poll poll) throws ResourceValidationException {

		validatePoll(poll);

		poll.setId(pollIdGenerator.getAndIncrement());
		poll.getAnswers().forEach(answer -> answer.setId(answerIdGenerator.getAndIncrement()));

		polls.add(poll);
	}

	public void updatePollStatus(int pollId, String status) throws ResourceValidationException, ResourceNotFoundException, ResourceOperationConflictException {

		Poll poll = getPoll(pollId);

		PollStatus newPollStatus;

		try {

			newPollStatus = PollStatus.valueOf(status);

		} catch (IllegalArgumentException e) {

			throw new ResourceValidationException("unknown poll status");
		}

		PollStatus currentPollStatus = poll.getStatus();

		if ((newPollStatus == PollStatus.STARTED && currentPollStatus == PollStatus.CREATED) ||
				(newPollStatus == PollStatus.CLOSED && currentPollStatus == PollStatus.STARTED)) {

			poll.setStatus(newPollStatus);

		} else {

			throw new ResourceOperationConflictException("can't change poll status from " + currentPollStatus + " to " + newPollStatus);
		}

	}

	public void submitVote(int pollId, Vote vote) throws ResourceNotFoundException, ResourceOperationConflictException {

		Poll poll = getPoll(pollId);

		if (poll.getStatus() != PollStatus.STARTED) {

			throw new ResourceOperationConflictException("poll status needs to be " + PollStatus.STARTED);
		}
		
		if (poll.getAnswers().stream().noneMatch(answer -> answer.getId() == vote.getAnswerId())){
			
			throw new ResourceOperationConflictException("poll has no answer with id: " + vote.getAnswerId());
		}

		vote.setId(voteIdGenerator.getAndIncrement());

		poll.getVotes().add(vote);
	}

	public String[] getPollStatistic(int pollId) throws ResourceNotFoundException {

		Poll poll = getPoll(pollId);

		Map<Integer, Long> answerIdVoteCountMap = poll.getVotes().stream()
				.collect(Collectors.groupingBy(Vote::getAnswerId, Collectors.counting()));

		String[] statistic = poll.getAnswers().stream()
				.map(answer -> answer.getText() + " - " + answerIdVoteCountMap.computeIfAbsent(answer.getId(), key -> 0l))
				.toArray(String[]::new);

		return statistic;
	}

}
