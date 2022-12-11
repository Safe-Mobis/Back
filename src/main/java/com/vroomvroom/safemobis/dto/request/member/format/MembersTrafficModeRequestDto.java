package com.vroomvroom.safemobis.dto.request.member.format;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
public class MembersTrafficModeRequestDto {

    @NotBlank
    private TrafficCode trafficCode;

    @NotNull
    private boolean bicycleFlag;

    @NotNull
    private boolean carFlag;

    @NotNull
    private boolean childFlag;

    @NotNull
    private boolean kickBoardFlag;

    @NotNull
    private boolean motorcycleFlag;

    @NotNull
    private boolean pedestrianFlag;

}
