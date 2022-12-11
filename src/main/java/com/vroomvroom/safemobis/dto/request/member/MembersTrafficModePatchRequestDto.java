package com.vroomvroom.safemobis.dto.request.member;

import com.vroomvroom.safemobis.dto.request.member.format.MembersTrafficModeRequestDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MembersTrafficModePatchRequestDto {

    @NotNull
    private List<MembersTrafficModeRequestDto> trafficMode;

}
