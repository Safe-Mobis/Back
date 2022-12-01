package com.vroomvroom.safemobis.dto.response.member.format;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class MembersTrafficModeResponseDto {

    @NotBlank
    private TrafficCode trafficCode;

    @NotNull
    private boolean bicycleFlag;

    @NotNull
    private boolean carFlag;

    @NotNull
    private boolean childFlag;

    @NotNull
    private boolean kickboardFlag;

    @NotNull
    private boolean motorcycleFlag;

    @NotNull
    private boolean pedestrianFlag;

}
