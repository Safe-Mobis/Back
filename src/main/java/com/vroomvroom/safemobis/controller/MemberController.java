package com.vroomvroom.safemobis.controller;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.dto.request.member.MembersLoginPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPositionPutRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPostRequestDto;
import com.vroomvroom.safemobis.dto.response.base.BaseResponse;
import com.vroomvroom.safemobis.dto.response.member.MembersPositionPutResponseDto;
import com.vroomvroom.safemobis.dto.response.member.TokenInfo;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Collections;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.CAR;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody MembersPostRequestDto membersPostRequestDto, HttpServletRequest request) {
        Member member = Member.builder()
                .username(membersPostRequestDto.getUsername())
                .password(membersPostRequestDto.getPassword())
                .trafficCode(CAR)
                .roles(Collections.singletonList("USER"))
                .build();
        memberService.save(member);
        return new ResponseEntity<>(BaseResponse.of(request, CREATED.value()), CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody MembersLoginPostRequestDto membersLoginPostRequestDto, HttpServletRequest request) {
        String username = membersLoginPostRequestDto.getUsername();
        String password = membersLoginPostRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(username, password);
        return new ResponseEntity<>(BaseResponse.of(request, OK.value(), tokenInfo), OK);
    }

    @PutMapping("/position")
    public ResponseEntity<BaseResponse> updatePosition(@Valid @RequestBody MembersPositionPutRequestDto membersPositionPutRequestDto, HttpServletRequest request) {
        Position updatePosition = Position.builder()
                .x(membersPositionPutRequestDto.getX())
                .y(membersPositionPutRequestDto.getX())
                .direction(membersPositionPutRequestDto.getDirection())
                .velocity(membersPositionPutRequestDto.getVelocity())
                .build();
        MembersPositionPutResponseDto membersPositionPutResponseDto = memberService.updatePosition(updatePosition);
        return new ResponseEntity<>(BaseResponse.of(request, OK.value(), membersPositionPutResponseDto), OK);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test success!!", OK);
    }
}
