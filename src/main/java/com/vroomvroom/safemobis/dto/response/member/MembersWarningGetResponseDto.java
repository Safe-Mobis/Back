package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import com.vroomvroom.safemobis.dto.response.member.format.MembersWarningResponseDto;
import com.vroomvroom.safemobis.dto.response.member.format.MembersPositionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MembersWarningGetResponseDto extends BaseModel {

    List<MembersPositionResponseDto> surroundingMembers;
    private WarningCode warningCode;
    private List<MembersWarningResponseDto> warningMembers;

    public static MembersWarningGetResponseDto of(List<Member> surroundingMembers, WarningCode warningCode, List<Member> warningMembers) {
        List<MembersPositionResponseDto> membersPositionResponseDtos = surroundingMembers.stream()
                .map(MembersPositionResponseDto::new)
                .collect(toList());
        List<MembersWarningResponseDto> membersWarningResponseDtos = warningMembers.stream()
                .map(MembersWarningResponseDto::new)
                .collect(toList());
        return new MembersWarningGetResponseDto(membersPositionResponseDtos, warningCode, membersWarningResponseDtos);
    }
}
