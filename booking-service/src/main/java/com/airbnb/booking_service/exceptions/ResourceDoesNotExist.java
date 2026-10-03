package com.airbnb.booking_service.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceDoesNotExist extends ApiException {
    public ResourceDoesNotExist(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
