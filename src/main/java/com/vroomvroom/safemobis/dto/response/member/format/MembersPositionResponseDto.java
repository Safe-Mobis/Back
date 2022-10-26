package com.vroomvroom.safemobis.dto.response.member.format;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.Data;

@Data
public class MembersPositionResponseDto {

    private String username;
    private TrafficCode trafficCode;
    private Double x;
    private Double y;

    public MembersPositionResponseDto(Member member) {
        username = member.getUsername();
        trafficCode = member.getTrafficCode();
        x = member.getPosition().getX();
        y = member.getPosition().getY();
    }
}
