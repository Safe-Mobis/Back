package com.vroomvroom.safemobis.error.exception;

import com.vroomvroom.safemobis.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
