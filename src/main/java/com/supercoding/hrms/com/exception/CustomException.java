package com.supercoding.hrms.com.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final CustomMessage customMessage;

    public CustomException(CustomMessage customMessage) {
        super(customMessage.getMessage());
        this.customMessage = customMessage;
    }
}
