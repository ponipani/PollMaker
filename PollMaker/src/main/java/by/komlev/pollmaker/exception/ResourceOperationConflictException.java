package by.komlev.pollmaker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceOperationConflictException extends Exception {

	public ResourceOperationConflictException(String message) {
		super(message);
	}

}
