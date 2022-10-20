package com.vroomvroom.safemobis.dto.request.member;

import lombok.Data;

@Data
public class MembersLoginPostRequestDto {

    private String username;
    private String password;
}
