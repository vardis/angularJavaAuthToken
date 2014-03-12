package server.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * General error handler for the application.
 */
@ControllerAdvice
class ExceptionHandlerAdvice {

    private final static Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = IOException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleIOException(Exception exception) {
        LOG.error("Server error", exception);
    }

	/**
	 * Handle exceptions thrown by handlers.
	 */
	@ExceptionHandler(value = InvalidServiceCallException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleClientError(Exception exception) {
        LOG.error("Client error", exception);
	}
}