package com.vroomvroom.safemobis.dto.request.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MembersPostRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
