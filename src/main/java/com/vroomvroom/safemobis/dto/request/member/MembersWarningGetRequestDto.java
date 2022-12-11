package com.vroomvroom.safemobis.dto.request.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MembersWarningGetRequestDto {

    @NotNull
    private Long intersectionId;
}
