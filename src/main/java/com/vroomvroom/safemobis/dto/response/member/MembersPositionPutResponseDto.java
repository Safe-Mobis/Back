package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import com.vroomvroom.safemobis.dto.response.member.format.MembersPositionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MembersPositionPutResponseDto extends BaseModel {

    List<MembersPositionResponseDto> surroundingMembers;

    public static MembersPositionPutResponseDto from(List<Member> members) {
        List<MembersPositionResponseDto> membersPositionResponseDtos = members.stream()
                .map(MembersPositionResponseDto::new)
                .collect(Collectors.toList());
        return new MembersPositionPutResponseDto(membersPositionResponseDtos);
    }
}
