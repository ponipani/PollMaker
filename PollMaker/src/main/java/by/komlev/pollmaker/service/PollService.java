package by.komlev.pollmaker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import by.komlev.pollmaker.exception.ResourceNotFoundException;
import by.komlev.pollmaker.exception.ResourceOperationConflictException;
import by.komlev.pollmaker.exception.ResourceValidationException;
import by.komlev.pollmaker.model.Answer;
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

		Poll result = polls.stream().filter(poll -> poll.getId() == pollId).findAny().orElse(null);

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

	public void updatePollStatus(Poll poll, String status) throws ResourceValidationException, ResourceNotFoundException, ResourceOperationConflictException {

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

	public void submitVote(int pollId, int answerId) throws ResourceNotFoundException, ResourceOperationConflictException {

		Poll poll = getPoll(pollId);

		if (poll.getStatus() != PollStatus.STARTED) {
			throw new ResourceOperationConflictException("poll status needs to be " + PollStatus.STARTED);
		}

		Answer answer = poll.getAnswers().stream().filter(answ -> answ.getId() == answerId).findAny().orElse(null);

		if (answer == null) {
			throw new ResourceOperationConflictException("poll has no answer with id: " + answerId);
		}

		Vote vote = new Vote();
		vote.setId(voteIdGenerator.getAndIncrement());

		answer.getVotes().add(vote);
	}

	public String[] getPollStatistic(int pollId) throws ResourceNotFoundException {

		Poll poll = getPoll(pollId);

		String[] statistic = poll.getAnswers().stream()
				.map(answer -> answer.getText() + " - " + answer.getVotes().size())
				.toArray(String[]::new);

		return statistic;
	}

}
