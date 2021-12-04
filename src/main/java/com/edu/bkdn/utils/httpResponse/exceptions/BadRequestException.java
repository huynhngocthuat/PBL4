package com.edu.bkdn.utils.httpResponse.exceptions;

import com.edu.bkdn.utils.httpResponse.HttpResponse;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpResponse {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
    }

}
