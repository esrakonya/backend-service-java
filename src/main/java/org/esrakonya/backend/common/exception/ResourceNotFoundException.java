package org.esrakonya.backend.common.exception;

/**
 * Custom exception to be thrown when a database resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
