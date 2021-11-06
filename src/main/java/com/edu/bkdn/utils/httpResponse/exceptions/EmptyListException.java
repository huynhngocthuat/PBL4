package com.edu.bkdn.utils.httpResponse.exceptions;

import com.edu.bkdn.utils.httpResponse.HttpResponse;
import org.springframework.http.HttpStatus;

public class EmptyListException extends HttpResponse {
    public EmptyListException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

    public EmptyListException(){
        super(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
    }
}
