package by.komlev.pollmaker.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import by.komlev.pollmaker.model.Poll;
import by.komlev.pollmaker.model.PollStatus;

@Component
public class PollResourceAssembler implements ResourceAssembler<Poll, Resource<Poll>> {

	@Override
	public Resource<Poll> toResource(Poll poll) {

		Resource<Poll> resource = new Resource<>(poll);

		resource.add(linkTo(PollController.class).slash("poll").slash(poll.getId()).withSelfRel());
		resource.add(linkTo(PollController.class).slash("poll").slash(poll.getId()).slash("statistic").withRel("statistic"));

		if (poll.getStatus() == PollStatus.STARTED) {
			resource.add(linkTo(PollController.class).slash("poll").slash(poll.getId()).slash("votes").withRel("vote"));
		}

		return resource;
	}

}
