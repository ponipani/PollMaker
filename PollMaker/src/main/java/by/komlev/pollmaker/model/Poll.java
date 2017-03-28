package by.komlev.pollmaker.model;

import java.util.ArrayList;
import java.util.List;

public class Poll {

	int id;

	String theme = "";

	PollStatus status = PollStatus.CREATED;

	List<Answer> answers = new ArrayList<>();

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

}
