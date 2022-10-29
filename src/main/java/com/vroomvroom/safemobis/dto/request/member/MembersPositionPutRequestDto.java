package com.vroomvroom.safemobis.dto.request.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MembersPositionPutRequestDto {

    @NotNull
    private double x;
    @NotNull
    private double y;
    @NotNull
    private double direction;
    @NotNull
    private double velocity;
    @NotNull
    private double acceleration;
}
