package com.vroomvroom.safemobis.controller;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Path;
import com.vroomvroom.safemobis.dto.request.member.MembersLoginPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPathPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.format.MembersPathRequestDto;
import com.vroomvroom.safemobis.dto.response.base.BaseResponse;
import com.vroomvroom.safemobis.dto.response.member.MembersIntersectionsGetResponseDto;
import com.vroomvroom.safemobis.dto.response.member.TokenInfo;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.CAR;
import static com.vroomvroom.safemobis.security.SecurityUtil.getCurrentUsername;
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
        String username = membersPostRequestDto.getUsername();
        String password = membersPostRequestDto.getPassword();
        Member member = getMember(username, password);
        memberService.save(member);
        return new ResponseEntity<>(BaseResponse.of(request, CREATED.value()), CREATED);
    }

    private Member getMember(String username, String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .trafficCode(CAR)
                .roles(Collections.singletonList("USER"))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody MembersLoginPostRequestDto membersLoginPostRequestDto, HttpServletRequest request) {
        String username = membersLoginPostRequestDto.getUsername();
        String password = membersLoginPostRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(username, password);
        return new ResponseEntity<>(BaseResponse.of(request, OK.value(), tokenInfo), OK);
    }

    @PostMapping("/path")
    public ResponseEntity<BaseResponse> savePath(@Valid @RequestBody MembersPathPostRequestDto membersPathPostRequestDto, HttpServletRequest request) {
        List<MembersPathRequestDto> membersPathRequestDtos = membersPathPostRequestDto.getRoute();
        Path path = getPath(membersPathRequestDtos);
        memberService.savePath(path);
        return new ResponseEntity<>(BaseResponse.of(request, CREATED.value()), CREATED);
    }

    private Path getPath(List<MembersPathRequestDto> membersPathRequestDtos) {
        Member member = memberService.findByUsername(getCurrentUsername());
        LineString route = getRoute(membersPathRequestDtos);
        return Path.builder()
                .route(route)
                .member(member)
                .build();
    }

    private LineString getRoute(List<MembersPathRequestDto> route) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate[] coordinates = new Coordinate[route.size()];
        for (int i = 0; i < route.size(); i++) {
            coordinates[i] = new Coordinate(route.get(i).getLatitude(), route.get(i).getLongitude());
        }
        return geometryFactory.createLineString(coordinates);
    }

    @GetMapping("/intersections")
    public ResponseEntity<BaseResponse> getIntersections(HttpServletRequest request) {
        MembersIntersectionsGetResponseDto membersIntersectionsGetResponseDto = memberService.getIntersections();
        return new ResponseEntity<>(BaseResponse.of(request, OK.value(), membersIntersectionsGetResponseDto), OK);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test success!!", OK);
    }
}
