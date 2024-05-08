package com.user.management.advice;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
     * 발생한 예외의 메시지, 상태 코드, 발생 시간을 JSON 형태로 응답 본문에 포함하여 반환합니다.
     *
     * @param e 발생한 RuntimeException
     * @return 응답 본문에 에러 정보를 담은 ResponseEntity
     * @throws JSONException JSON 객체 생성 시 발생할 수 있는 예외
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) throws JSONException {

        JSONObject errorDetails = new JSONObject();
        try {
            errorDetails.put("title", e.getMessage());
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            errorDetails.put("timestamp", LocalDateTime.now().toString());
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDetails.toString(4));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
