package com.vroomvroom.safemobis.dto.response.member.format;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MembersWarningResponseDto {

    private String username;
    private TrafficCode trafficCode;
    private double direction;

    public MembersWarningResponseDto(Member member) {
        username = member.getUsername();
        trafficCode = member.getTrafficCode();
        direction = member.getPosition().getDirection();
    }
}
