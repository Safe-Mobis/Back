package com.vroomvroom.safemobis.error;

import com.vroomvroom.safemobis.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.vroomvroom.safemobis.error.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("handleMethodArgumentNotValidException", e);
        return new ResponseEntity<>(ErrorResponse.of(request, INVALID_INPUT_VALUE, e.getBindingResult()), BAD_REQUEST);
    }

    /**
     * ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest request) {
        log.error("handleBindException", e);
        return new ResponseEntity<>(ErrorResponse.of(request, INVALID_INPUT_VALUE, e.getBindingResult()), BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     * 컨트롤러에서 매칭되는 url이 없기 때문에 path를 알 수 없음.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return new ResponseEntity<>(ErrorResponse.of(request, METHOD_NOT_FOUND, e.getMessage()), METHOD_NOT_ALLOWED);
    }

    /**
     * 비즈니스 요구사항에 따른 Exception
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("businessException", e);
        return new ResponseEntity<>(ErrorResponse.of(request, e.getErrorCode(), e.getMessage()), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    /**
     * 그 밖에 발생하는 모든 예외 처리, NullPointException 등
     * 개발자가 직접 핸들링해서 다른 예외로 던지지 않으면 모두 이곳으로 모인다.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("handleException", e);
        return new ResponseEntity<>(ErrorResponse.of(request, SERVER_ERROR, e.getMessage()), INTERNAL_SERVER_ERROR);
    }
}
