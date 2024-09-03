package com.infinbank.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Data Transfer Object (DTO) for ErrorResponse object.
 * <p>
 * This class is used to display meaningful error message when they happen in the program.
 * It contains fields for the status code, message, and detailed, error list.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private HttpStatus statusCode;

    private String message;

    private Map<String, String> errors;
}
