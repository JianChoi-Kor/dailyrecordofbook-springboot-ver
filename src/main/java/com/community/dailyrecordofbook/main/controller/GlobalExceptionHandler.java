package com.community.dailyrecordofbook.main.controller;

import groovy.util.logging.Slf4j;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = HttpClientErrorException.Unauthorized.class)
    public String unauthorizedException (Model model, HttpClientErrorException.Unauthorized e) {
        model.addAttribute ("status", HttpStatus.UNAUTHORIZED.value ());
        model.addAttribute ("errorMsg", e.getMessage () == null ? HttpStatus.UNAUTHORIZED.getReasonPhrase () : e.getMessage ());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public String notFoundException (Model model, NotFoundException e) {
        model.addAttribute ("status", HttpStatus.NOT_FOUND.value ());
        model.addAttribute ("errorMsg", e.getMessage () == null ? HttpStatus.NOT_FOUND.getReasonPhrase () : e.getMessage ());
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpClientErrorException.BadRequest.class)
    public String badRequestException (Model model, HttpClientErrorException.BadRequest e) {
        model.addAttribute ("status", HttpStatus.BAD_REQUEST.value ());
        model.addAttribute ("errorMsg", e.getMessage () == null ? HttpStatus.BAD_REQUEST.getReasonPhrase () : e.getMessage ());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = Exception.class)
    public String exception (Model model, Exception e, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute ("status", HttpStatus.NOT_FOUND.value ());
        return "error";
    }
}
