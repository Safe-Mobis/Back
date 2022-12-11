package com.vroomvroom.safemobis.dto.request.member;

import com.vroomvroom.safemobis.dto.request.member.format.MembersPathRequestDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MembersPathPostRequestDto {

    @NotNull
    private List<MembersPathRequestDto> route;
}
