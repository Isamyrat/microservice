package com.example.controller;

import com.example.dto.ErrorMessageDto;
import com.example.exception.BadRequestException;
import com.example.exception.ForbiddenException;
import com.example.exception.NotFoundException;
import com.example.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody
    ErrorMessageDto notFound(NotFoundException e) {
        return new ErrorMessageDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody
    ErrorMessageDto badRequest(BadRequestException e) {
        return new ErrorMessageDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public @ResponseBody
    ErrorMessageDto forbidden(ForbiddenException e) {
        return new ErrorMessageDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody
    ErrorMessageDto jwtExpired(UnauthorizedException e) {
        return new ErrorMessageDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    List<ErrorMessageDto> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
            .map(i -> new ErrorMessageDto(i.getDefaultMessage()))
            .collect(Collectors.toList());
    }
}
