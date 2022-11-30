package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MembersTrafficCodeGetResponseDto extends BaseModel {

    @NotBlank
    private TrafficCode trafficCode;

    public static MembersTrafficCodeGetResponseDto from(Member member) {
        return MembersTrafficCodeGetResponseDto.builder()
                .trafficCode(member.getTrafficCode())
                .build();
    }
}
