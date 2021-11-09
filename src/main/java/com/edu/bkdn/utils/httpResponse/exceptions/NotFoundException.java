package com.edu.bkdn.utils.httpResponse.exceptions;

import com.edu.bkdn.utils.httpResponse.HttpResponse;
import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpResponse {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

}
