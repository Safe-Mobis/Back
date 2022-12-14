package com.vroomvroom.safemobis.dto.request.member;

import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import lombok.Data;

@Data
public class MembersWarningPositionGetRequestDto {

    private Long intersectionId;
    private WarningCode warningCode;
    private double latitude;
    private double longitude;
}
