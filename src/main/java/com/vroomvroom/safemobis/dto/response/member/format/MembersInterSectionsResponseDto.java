package com.vroomvroom.safemobis.dto.response.member.format;

import com.vroomvroom.safemobis.domain.Intersection;
import lombok.Data;

@Data
public class MembersInterSectionsResponseDto {

    private Long intersectionId;
    private MembersIntersectionDto position;

    public MembersInterSectionsResponseDto(Intersection intersection) {
        intersectionId = intersection.getId();
        position = new MembersIntersectionDto(intersection.getPosition().getX(), intersection.getPosition().getY());
    }
}
