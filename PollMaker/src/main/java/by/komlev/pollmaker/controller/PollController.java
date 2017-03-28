package by.komlev.pollmaker.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.komlev.pollmaker.exception.ResourceNotFoundException;
import by.komlev.pollmaker.exception.ResourceOperationConflictException;
import by.komlev.pollmaker.exception.ResourceValidationException;
import by.komlev.pollmaker.model.Poll;
import by.komlev.pollmaker.model.Vote;
import by.komlev.pollmaker.service.PollService;

@RestController
@RequestMapping("/api")
public class PollController {

	@Autowired
	PollService pollService;

	@Autowired
	PollResourceAssembler pollResourceAssembler;

	@PostMapping("/polls")
	public ResponseEntity<Void> postPoll(@RequestBody Poll poll) throws ResourceValidationException {

		pollService.savePoll(poll);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(linkTo(PollController.class).slash("poll").slash(poll.getId()).toUri());

		return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
	}

	@GetMapping("/poll/{id}")
	public ResponseEntity<Resource<Poll>> getPoll(@PathVariable("id") int pollId) throws ResourceNotFoundException {

		Poll poll = pollService.getPoll(pollId);

		return ResponseEntity.ok(pollResourceAssembler.toResource(poll));
	}

	@PatchMapping("/poll/{id}")
	public ResponseEntity<Resource<Poll>> updatePoll(@PathVariable("id") int pollId, @RequestBody Map<String, Object> requestBody)
			throws ResourceValidationException, ResourceNotFoundException, ResourceOperationConflictException {

		if (requestBody.containsKey("status")) {

			Poll poll = pollService.getPoll(pollId);

			pollService.updatePollStatus(poll, (String) requestBody.get("status"));

			return ResponseEntity.ok(pollResourceAssembler.toResource(poll));

		} else {

			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/poll/{id}/votes")
	public ResponseEntity<Void> submitVote(@PathVariable("id") int pollId, @RequestBody Map<String, Object> requestBody)
			throws ResourceNotFoundException, ResourceOperationConflictException {

		if (requestBody.containsKey("answerId")) {
			
			pollService.submitVote(pollId, (int) requestBody.get("answerId"));

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(linkTo(PollController.class).slash("poll").slash(pollId).toUri());

			return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
			
		} else {

			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/poll/{id}/statistic")
	public ResponseEntity<String[]> getPollStatistic(@PathVariable("id") int pollId) throws ResourceNotFoundException {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(linkTo(PollController.class).slash("poll").slash(pollId).toUri());

		String[] statistic = pollService.getPollStatistic(pollId);

		return new ResponseEntity<>(statistic, httpHeaders, HttpStatus.OK);
	}

}
