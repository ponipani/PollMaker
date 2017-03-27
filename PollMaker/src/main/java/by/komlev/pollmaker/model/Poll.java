package by.komlev.pollmaker.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Poll {
	
	int id;

	String theme = "";
	
	PollStatus status = PollStatus.CREATED;

	List<Answer> answers = new ArrayList<>();

	@JsonIgnore
	List<Vote> votes = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public PollStatus getStatus() {
		return status;
	}

	public void setStatus(PollStatus status) {
		this.status = status;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}


}
