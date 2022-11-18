package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.domain.PathIntersection;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MembersWarningGetResponseDto extends BaseModel {

    private WarningCode warningCode;
    private TrafficCode trafficCode;
    private double latitude;
    private double longitude;

    public static MembersWarningGetResponseDto from(PathIntersection pathIntersection) {
        return MembersWarningGetResponseDto.builder()
                .warningCode(pathIntersection.getWarningCode())
                .trafficCode(pathIntersection.getPath().getMember().getTrafficCode())
                .latitude(pathIntersection.getWarningPosition().getX())
                .longitude(pathIntersection.getWarningPosition().getY())
                .build();
    }
}
