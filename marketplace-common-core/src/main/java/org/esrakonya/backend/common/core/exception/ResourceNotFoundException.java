package org.esrakonya.backend.common.core.exception;

/**
 * Custom exception to be thrown when a database resource is not found.
 */
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
