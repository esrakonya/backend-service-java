package org.esrakonya.backend.common.core.exception;

import com.fasterxml.jackson.databind.ser.Serializers;

public class InsufficientStockException extends BaseException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
