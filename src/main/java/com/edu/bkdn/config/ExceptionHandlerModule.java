package com.edu.bkdn.config;

import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
// Class for handling exceptions throw by controllers
public class ExceptionHandlerModule {

    @ExceptionHandler(value = { NotFoundException.class })
    @ResponseBody
    public String handleNotFoundException(NotFoundException error, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(error.getStatus());
        return new Gson().toJson(error);
    }

    @ExceptionHandler(value = { DuplicateException.class })
    @ResponseBody
    public String handleDuplicateException(DuplicateException error, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(error.getStatus());
        return new Gson().toJson(error);
    }
}
