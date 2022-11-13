package com.vroomvroom.safemobis.dto.request.member.format;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MembersPathRequestDto {

    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
}
