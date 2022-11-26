package com.vroomvroom.safemobis.dto.request.member;

import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import lombok.Data;

@Data
public class MembersWarningPositionPutRequestDto {

    private Long intersectionId;
    private WarningCode warningCode;
    private double latitude;
    private double longitude;

    public static class MembersTrafficCodePutRequestDto {
    }
}
