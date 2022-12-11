package com.vroomvroom.safemobis.dto.request.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MembersLoginPostRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
