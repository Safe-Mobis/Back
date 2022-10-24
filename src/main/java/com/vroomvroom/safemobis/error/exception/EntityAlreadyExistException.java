package com.vroomvroom.safemobis.error.exception;

import com.vroomvroom.safemobis.error.ErrorCode;

public class EntityAlreadyExistException extends BusinessException{
    public EntityAlreadyExistException(String message) {
        super(message, ErrorCode.ENTITY_ALREADY_EXIST);
    }
}
