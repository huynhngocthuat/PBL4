package com.edu.bkdn.utils.httpResponse;

public class HttpResponse extends Throwable{
    private final String message;

    private final int status;

    public HttpResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
