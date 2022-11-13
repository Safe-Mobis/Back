package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.domain.Intersection;
import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import com.vroomvroom.safemobis.dto.response.member.format.MembersInterSectionsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MembersIntersectionsGetResponseDto extends BaseModel {

    List<MembersInterSectionsResponseDto> intersections;

    public static MembersIntersectionsGetResponseDto from(List<Intersection> intersections) {
        List<MembersInterSectionsResponseDto> membersInterSectionsResponseDtos = intersections.stream()
                .map(MembersInterSectionsResponseDto::new)
                .collect(Collectors.toList());
        return new MembersIntersectionsGetResponseDto(membersInterSectionsResponseDtos);
    }
}
