package com.vroomvroom.safemobis.service;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.domain.TrafficMode;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import com.vroomvroom.safemobis.dto.response.member.MembersPositionPutResponseDto;
import com.vroomvroom.safemobis.dto.response.member.TokenInfo;
import com.vroomvroom.safemobis.error.exception.EntityAlreadyExistException;
import com.vroomvroom.safemobis.error.exception.EntityNotFoundException;
import com.vroomvroom.safemobis.repository.MemberRepository;
import com.vroomvroom.safemobis.security.SecurityUtil;
import com.vroomvroom.safemobis.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.*;
import static java.lang.Boolean.TRUE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void save(Member member) {
        if (isMemberExists(member.getUsername())) {
            throw new EntityAlreadyExistException("[" + member.getUsername() + "] 이미 회원가입된 아이디입니다.");
        }

        List<TrafficMode> trafficModes = new ArrayList<>();
        for (TrafficCode trafficCode : TrafficCode.values()) {
            trafficModes.add(TrafficMode.builder()
                    .trafficCode(trafficCode)
                    .carFlag(TRUE)
                    .pedestrianFlag(TRUE)
                    .childFlag(TRUE)
                    .kickBoardFlag(TRUE)
                    .bicycleFlag(TRUE)
                    .motorcycleFlag(TRUE)
                    .member(member)
                    .build());
        }
        member.setTrafficModes(trafficModes);
        memberRepository.save(member);
    }

    private boolean isMemberExists(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public TokenInfo login(String username, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public MembersPositionPutResponseDto updatePosition(Position updatePosition) {
        Member member = findMember(SecurityUtil.getCurrentUsername());
        member.getPosition().updatePosition(updatePosition);
        return MembersPositionPutResponseDto.from(getSurroundMembers(member));
    }

    public Member findMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("[" + username + "] 회원가입이 되어있지 않습니다."));
    }

    private List<Member> getSurroundMembers(Member member) {
        final double RADIUS = 0.05;
        Map<TrafficCode, Boolean> trafficWarningMap = getTrafficWarningMap(member);
        Position position = member.getPosition();
        List<Member> members = memberRepository.findAll();
        List<Member> surroundingMembers = new ArrayList<>();
        for (Member surroundingMember : members) {
            if (member.getId().equals(surroundingMember.getId())) {
                continue;
            }
            Position surroundingPosition = surroundingMember.getPosition();
            if (trafficWarningMap.get(surroundingMember.getTrafficCode())) {
                if (Math.pow(RADIUS, 2) >= (Math.pow(position.getX() - surroundingPosition.getX(), 2) + Math.pow(position.getY() - surroundingPosition.getY(), 2))) {
                    surroundingMembers.add(surroundingMember);
                }
            }
        }
        return surroundingMembers;
    }

    private Map<TrafficCode, Boolean> getTrafficWarningMap(Member member) {
        TrafficMode trafficMode = member.getCurrentTrafficMode();
        Map<TrafficCode, Boolean> warningMap = new HashMap<>();
        warningMap.put(CAR, trafficMode.getCarFlag());
        warningMap.put(PEDESTRIAN, trafficMode.getPedestrianFlag());
        warningMap.put(CHILD, trafficMode.getChildFlag());
        warningMap.put(KICK_BOARD, trafficMode.getKickBoardFlag());
        warningMap.put(BICYCLE, trafficMode.getBicycleFlag());
        warningMap.put(MOTORCYCLE, trafficMode.getMotorcycleFlag());
        return warningMap;
    }
}
