package com.vroomvroom.safemobis.controller;

import com.vroomvroom.safemobis.dto.request.member.MembersLoginPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPostRequestDto;
import com.vroomvroom.safemobis.dto.response.member.TokenInfo;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody MembersPostRequestDto membersPostRequestDto) {
        String username = membersPostRequestDto.getUsername();
        String password = membersPostRequestDto.getPassword();
        memberService.save(username, password);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody MembersLoginPostRequestDto membersLoginPostRequestDto) {
        String username = membersLoginPostRequestDto.getUsername();
        String password = membersLoginPostRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(username, password);
        return new ResponseEntity(tokenInfo, HttpStatus.OK);
    }
}
