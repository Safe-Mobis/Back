package com.vroomvroom.safemobis.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private String path;

    public static ErrorResponse of(HttpServletRequest req, ErrorCode errorCode, String message){
        return new ErrorResponse(LocalDateTime.now(), errorCode.getStatus(), errorCode.getError(), message, req.getServletPath());
    }

    public static ErrorResponse of(HttpServletRequest request, ErrorCode errorCode, BindingResult bindingResult) {
        StringBuilder builder = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return of(request, errorCode, builder.toString());
    }
}
