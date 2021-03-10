package com.gft.teste.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gft.teste.exception.NotFoundException;
import com.gft.teste.presenter.ErrorPresenter;


@ControllerAdvice
public class ErrorExceptionHandler {

	@ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorPresenter> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
		return new ResponseEntity<>(new ErrorPresenter(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorPresenter> handleException(Exception ex, HttpServletRequest request) {
		return new ResponseEntity<>(new ErrorPresenter(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
