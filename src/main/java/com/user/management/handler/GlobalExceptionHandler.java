package com.user.management.handler;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

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

        return new ResponseEntity<>(errorDetails.toString(4), HttpStatus.BAD_REQUEST);
    }
}
