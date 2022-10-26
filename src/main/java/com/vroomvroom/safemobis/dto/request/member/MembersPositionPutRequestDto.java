package com.vroomvroom.safemobis.dto.request.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MembersPositionPutRequestDto {

    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotNull
    private Double direction;
    @NotNull
    private Double velocity;
}
