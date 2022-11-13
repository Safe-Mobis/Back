package com.vroomvroom.safemobis.controller;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Path;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.dto.request.member.MembersLoginPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPathPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPositionPutRequestDto;
import com.vroomvroom.safemobis.dto.request.member.MembersPostRequestDto;
import com.vroomvroom.safemobis.dto.request.member.format.MembersPathRequestDto;
import com.vroomvroom.safemobis.dto.response.base.BaseResponse;
import com.vroomvroom.safemobis.dto.response.member.MembersWarningGetResponseDto;
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

    @PostMapping("/path")
    public ResponseEntity<BaseResponse> savePath(@Valid @RequestBody MembersPathPostRequestDto membersPathPostRequestDto, HttpServletRequest request) {
        List<MembersPathRequestDto> route = membersPathPostRequestDto.getRoute();
        Path path = getPath(route);
        memberService.savePath(path);
        return new ResponseEntity<>(BaseResponse.of(request, CREATED.value()), CREATED);
    }

    private Path getPath(List<MembersPathRequestDto> route) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate[] coordinates = new Coordinate[route.size()];
        for (int i = 0; i < route.size(); i++) {
            coordinates[i] = new Coordinate(route.get(i).getLatitude(), route.get(i).getLongitude());
        }
        LineString lineString = geometryFactory.createLineString(coordinates);
        return Path.builder()
                .route(lineString)
                .member(memberService.findByUsername(getCurrentUsername()))
                .build();
    }

    @PutMapping("/position")
    public ResponseEntity<BaseResponse> updatePosition(@Valid @RequestBody MembersPositionPutRequestDto membersPositionPutRequestDto, HttpServletRequest request) {
        Position updatePosition = Position.builder()
                .x(membersPositionPutRequestDto.getX())
                .y(membersPositionPutRequestDto.getY())
                .direction(membersPositionPutRequestDto.getDirection())
                .velocity(membersPositionPutRequestDto.getVelocity())
                .acceleration(membersPositionPutRequestDto.getAcceleration())
                .build();
        memberService.updatePosition(updatePosition);
        return new ResponseEntity<>(BaseResponse.of(request, OK.value()), OK);
    }

    @GetMapping("/warning")
    public ResponseEntity<BaseResponse> getWarning(HttpServletRequest request) throws Exception {
        long beforeTime = System.currentTimeMillis();
        MembersWarningGetResponseDto membersWarningGetResponseDto = memberService.getSurroundMembersAndWarning();
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);
        return new ResponseEntity<>(BaseResponse.of(request, OK.value(), membersWarningGetResponseDto), OK);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test success!!", OK);
    }
}
