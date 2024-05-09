package com.user.management.advice;

import com.user.management.dto.ApiExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 모든 컨트롤러에서 발생하는 RuntimeException을 처리합니다.
 *
 * @author jjunho50
 * @version 1.0.0
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * RuntimeException을 처리하는 메소드입니다.
     * 발생한 예외의 발생 시간, 메시지를 ExceptionDto 형태로 응답 본문에 포함하여 반환합니다.
     *
     * @param e 발생한 RuntimeException
     * @return 응답 본문에 에러 정보를 담은 ResponseEntity
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiExceptionDto> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiExceptionDto(LocalDateTime.now(), e.getMessage()));
    }

    /**
     * 유효성 검증에서 발생한 예외를 처리하기 위한 메소드입니다.
     * 발생한 에러를 map 형태로 반환합니다.
     *
     * @param exception 발생한 MethodArgumentNotValidException
     * @return 응답 본문에 에러 map을 담은 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
