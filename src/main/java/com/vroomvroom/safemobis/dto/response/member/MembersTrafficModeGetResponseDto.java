package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import com.vroomvroom.safemobis.dto.response.member.format.MembersTrafficModeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class MembersTrafficModeGetResponseDto extends BaseModel {

    @NotNull
    private List<MembersTrafficModeResponseDto> trafficModes;

    public static MembersTrafficModeGetResponseDto from(List<MembersTrafficModeResponseDto> membersTrafficModeResponseDtos) {
        return new MembersTrafficModeGetResponseDto(membersTrafficModeResponseDtos);
    }

}
