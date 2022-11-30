package com.vroomvroom.safemobis.dto.request.member;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MembersTrafficCodePatchRequestDto {

    @NotNull
    private TrafficCode trafficCode;

}