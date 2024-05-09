package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 예외 처리 응답을 위한 DTO 클래스
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
public class ApiExceptionDto {
    private LocalDateTime time;
    private String message;
}
