package by.komlev.pollmaker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceValidationException extends Exception {
	
	public ResourceValidationException(String message) {
		super(message);
	}

}
