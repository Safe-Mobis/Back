package com.vroomvroom.safemobis.service;

import com.vroomvroom.safemobis.domain.*;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import com.vroomvroom.safemobis.dto.response.member.MembersWarningGetResponseDto;
import com.vroomvroom.safemobis.dto.response.member.TokenInfo;
import com.vroomvroom.safemobis.error.exception.EntityAlreadyExistException;
import com.vroomvroom.safemobis.error.exception.EntityNotFoundException;
import com.vroomvroom.safemobis.repository.MemberRepository;
import com.vroomvroom.safemobis.repository.PathRepository;
import com.vroomvroom.safemobis.security.SecurityUtil;
import com.vroomvroom.safemobis.security.jwt.JwtTokenProvider;
import com.vroomvroom.safemobis.util.Circle;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.vroomvroom.safemobis.domain.enumerate.WarningCode.SAFE;
import static com.vroomvroom.safemobis.domain.enumerate.WarningCode.WARN;
import static com.vroomvroom.safemobis.util.CircleUtil.getRadiusMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PathRepository pathRepository;

    @Transactional
    public void save(Member member) {
        if (isMemberExists(member.getUsername())) {
            throw new EntityAlreadyExistException("[" + member.getUsername() + "] 이미 회원가입된 아이디입니다.");
        }
        member.initializeTrafficModes();
        memberRepository.save(member);
    }

    private boolean isMemberExists(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public TokenInfo login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public void savePath(Path path) {
        List<PathIntersection> pathIntersections = getPathIntersections(path);
        path.setPathIntersections(pathIntersections);
        pathRepository.save(path);
    }

    private List<PathIntersection> getPathIntersections(Path path) {
        LineString route = path.getRoute();
        List<PathIntersection> pathIntersections = new ArrayList<>();
        List<Path> memberPaths = pathRepository.findAll();
        for (Path memberPath : memberPaths) {
            Geometry intersectionGeometry = route.intersection(memberPath.getRoute());
            int numPoints = intersectionGeometry.getNumPoints();
            if (numPoints == 1) {
                Point intersectionPoint = (Point) intersectionGeometry;
                addBothPathInterSection(pathIntersections, path, memberPath, intersectionPoint);
            } else {
                LineString lineString = (LineString) intersectionGeometry;
                for (int i = 0; i < numPoints; i++) {
                    Point intersectionPoint = lineString.getPointN(i);
                    addBothPathInterSection(pathIntersections, path, memberPath, intersectionPoint);
                }
            }
        }
        return pathIntersections;
    }

    private void addBothPathInterSection(List<PathIntersection> pathIntersections, Path path1, Path path2, Point intersectionPoint) {
        Intersection intersection = Intersection.builder()
                .position(intersectionPoint)
                .build();
        pathIntersections.add(PathIntersection.builder()
                .warningCode(SAFE)
                .path(path1)
                .intersection(intersection)
                .build());
        pathIntersections.add(PathIntersection.builder()
                .warningCode(SAFE)
                .path(path2)
                .intersection(intersection)
                .build());
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("[" + username + "] 회원가입이 되어있지 않습니다."));
    }

    @Transactional
    public void updatePosition(Position updatePosition) {
        Member member = findByUsername(SecurityUtil.getCurrentUsername());
        member.updatePosition(updatePosition);
    }

    public MembersWarningGetResponseDto getSurroundMembersAndWarning() throws Exception {
        Member member = findByUsername(SecurityUtil.getCurrentUsername());
        List<Member> surroundingMembers = getSurroundingMembers(member);
        List<Member> warningMembers = getWarningMembers(member, surroundingMembers);
        WarningCode warningCode = warningMembers.size() > 0 ? WARN : SAFE;
        return MembersWarningGetResponseDto.of(surroundingMembers, warningCode, warningMembers);
    }

    private List<Member> getSurroundingMembers(Member member) throws Exception {
        final double RADIUS = 50.0;
        Map<TrafficCode, Boolean> trafficWarningMap = member.getTrafficWarningMap();
        Position position = member.getPosition();
        List<Member> members = memberRepository.findAll();
        List<Member> surroundingMembers = new ArrayList<>();
        for (Member surroundingMember : members) {
            if (member.getId().equals(surroundingMember.getId())) {
                continue;
            }
            Circle circle = Circle.of(position.getX(), position.getY(), RADIUS);
            Position surroundingPosition = surroundingMember.getPosition();
            if (trafficWarningMap.get(surroundingMember.getTrafficCode())
                    && circle.isPositionInCircle(surroundingPosition.getX(), surroundingPosition.getY())) {
                surroundingMembers.add(surroundingMember);
            }
        }
        return surroundingMembers;
    }

    private static List<Member> getWarningMembers(Member member, List<Member> surroundingMembers) {
        final double TIME = 3.0;
        Map<TrafficCode, Double> radiusMap = getRadiusMap();
        Position beforePosition = member.getPosition();
        Position afterPosition = beforePosition.getPositionAfter(TIME);
        Circle circle = Circle.of(afterPosition.getX(), afterPosition.getY(), radiusMap.get(member.getTrafficCode()));
        List<Member> warningMembers = new ArrayList<>();
        for (Member surroundingMember : surroundingMembers) {
            Position beforeSurroundingPosition = surroundingMember.getPosition();
            Position afterSurroundingPosition = beforeSurroundingPosition.getPositionAfter(TIME);
            Circle surroundingCircle = Circle.of(afterSurroundingPosition.getX(), afterSurroundingPosition.getY(), radiusMap.get(surroundingMember.getTrafficCode()));
            if (circle.isOverlap(surroundingCircle)) {
                warningMembers.add(surroundingMember);
            }
        }
        return warningMembers;
    }
}
