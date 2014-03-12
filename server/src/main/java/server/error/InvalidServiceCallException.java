package server.error;

/**
 * This exception type indicates that a service call was not invoked
 * properly. This could be the result of a missing or invalid parameters.
 */
public class InvalidServiceCallException extends RuntimeException {

    public InvalidServiceCallException(String message) {
        super(message);
    }
}
