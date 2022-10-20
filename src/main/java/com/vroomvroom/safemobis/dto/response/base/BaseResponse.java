package com.vroomvroom.safemobis.dto.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
    private int status;
    private BaseModel data;
    private String path;

    private BaseResponse(LocalDateTime timeStamp, int status, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.path = path;
    }

    public static BaseResponse of(HttpServletRequest request, int status) {
        return new BaseResponse(LocalDateTime.now(), status, request.getServletPath());
    }

    public static BaseResponse of(HttpServletRequest request, int status, BaseModel data) {
        return new BaseResponse(LocalDateTime.now(), status, data, request.getServletPath());
    }
}
