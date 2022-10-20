package com.vroomvroom.safemobis.dto.response.member;

import com.vroomvroom.safemobis.dto.response.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenInfo extends BaseModel {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
