package com.vroomvroom.safemobis.service;

import com.vroomvroom.safemobis.domain.*;
import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import com.vroomvroom.safemobis.domain.Intersection;
import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Path;
import com.vroomvroom.safemobis.domain.PathIntersection;
import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import com.vroomvroom.safemobis.dto.request.member.format.MembersTrafficModeRequestDto;
import com.vroomvroom.safemobis.dto.response.member.*;
import com.vroomvroom.safemobis.dto.response.member.format.MembersTrafficModeResponseDto;
import com.vroomvroom.safemobis.error.exception.EntityAlreadyExistException;
import com.vroomvroom.safemobis.error.exception.EntityNotFoundException;
import com.vroomvroom.safemobis.repository.IntersectionRepository;
import com.vroomvroom.safemobis.repository.MemberRepository;
import com.vroomvroom.safemobis.repository.path.PathRepository;
import com.vroomvroom.safemobis.repository.pathintersection.PathIntersectionRepository;
import com.vroomvroom.safemobis.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.vroomvroom.safemobis.domain.enumerate.WarningCode.SAFE;
import static com.vroomvroom.safemobis.security.SecurityUtil.getCurrentUsername;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PathRepository pathRepository;
    private final PathIntersectionRepository pathIntersectionRepository;
    private final IntersectionRepository intersectionRepository;

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

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("[" + username + "] 회원가입이 되어있지 않습니다."));
    }

    @Transactional
    public void setTrafficCode(TrafficCode trafficCode) {
        Member member = findByUsername(getCurrentUsername());
        member.setTrafficCode(trafficCode);
    }

    public MembersTrafficCodeGetResponseDto getTrafficCode() {
        Member member = findByUsername(getCurrentUsername());
        return MembersTrafficCodeGetResponseDto.from(member);
    }

    @Transactional
    public void setTrafficMode(List<MembersTrafficModeRequestDto> membersTrafficModeRequestDtos) {
        Member member = findByUsername(getCurrentUsername());
        Map<TrafficCode, TrafficMode> map = new HashMap<>();
        for (TrafficMode t: member.getTrafficModes()) {
            map.put(t.getTrafficCode(), t);
        }

        for (MembersTrafficModeRequestDto m: membersTrafficModeRequestDtos) {
            TrafficMode t = map.get(m.getTrafficCode());
            t.setCarFlag(m.isCarFlag());
            t.setPedestrianFlag(m.isPedestrianFlag());
            t.setChildFlag(m.isChildFlag());
            t.setKickBoardFlag(m.isKickboardFlag());
            t.setBicycleFlag(m.isBicycleFlag());
            t.setMotorcycleFlag(m.isMotorcycleFlag());
        }
    }

    @Transactional
    public List<MembersTrafficModeResponseDto> getTrafficMode() {
        Member member = findByUsername(getCurrentUsername());
        List<TrafficMode> trafficModes = member.getTrafficModes();
        List<MembersTrafficModeResponseDto> membersTrafficModeResponseDtos = new ArrayList<>();
        for (TrafficMode t: trafficModes) {
            MembersTrafficModeResponseDto m = MembersTrafficModeResponseDto.builder()
                    .trafficCode(t.getTrafficCode())
                    .carFlag(t.isCarFlag())
                    .pedestrianFlag(t.isPedestrianFlag())
                    .childFlag(t.isChildFlag())
                    .kickboardFlag(t.isKickBoardFlag())
                    .bicycleFlag(t.isBicycleFlag())
                    .motorcycleFlag(t.isMotorcycleFlag())
                    .build();

            membersTrafficModeResponseDtos.add(m);
        }
        return membersTrafficModeResponseDtos;
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
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point defaultPoint = geometryFactory.createPoint(new Coordinate(-1.0, -1.0));
        Intersection intersection = Intersection.builder()
                .position(intersectionPoint)
                .build();
        pathIntersections.add(PathIntersection.builder()
                .warningCode(SAFE)
                .warningPosition(defaultPoint)
                .path(path1)
                .intersection(intersection)
                .build());
        pathIntersections.add(PathIntersection.builder()
                .warningCode(SAFE)
                .warningPosition(defaultPoint)
                .path(path2)
                .intersection(intersection)
                .build());
    }

    public MembersIntersectionsGetResponseDto getIntersections() {
        Member member = findByUsername(getCurrentUsername());
        List<Intersection> intersections = getIntersections(member);
        return MembersIntersectionsGetResponseDto.from(intersections);
    }

    private List<Intersection> getIntersections(Member member) {
        List<Intersection> intersections = new ArrayList<>();
        List<Path> paths = pathRepository.findByMemberWithIntersection(member);
        for (Path path : paths) {
            List<PathIntersection> pathIntersections = path.getPathIntersections();
            for (PathIntersection pathIntersection : pathIntersections) {
                intersections.add(pathIntersection.getIntersection());
            }
        }
        return intersections;
    }

    @Transactional
    public void saveWarningPosition(Long intersectionId, WarningCode warningCode, Point warningPosition) {
        Member member = findByUsername(getCurrentUsername());
        PathIntersection pathIntersection = findPathIntersectionByMemberAndIntersectionId(member, intersectionId, true);
        pathIntersection.setWarning(warningCode, warningPosition);
    }

    private PathIntersection findPathIntersectionByMemberAndIntersectionId(Member member, Long intersectionId, boolean withMeFlag) {
        Optional<PathIntersection> pathIntersectionOptional;
        if (withMeFlag) {
            pathIntersectionOptional = pathIntersectionRepository.findByMemberAndIntersectionId(member, intersectionId);
        } else {
            pathIntersectionOptional = pathIntersectionRepository.findByMemberNotAndIntersectionId(member, intersectionId);
        }
        return pathIntersectionOptional.orElseThrow(() -> new EntityNotFoundException("intersectionId [" + intersectionId + "] 해당 교차지점에 대한 경로가 없습니다."));
    }

    public MembersWarningGetResponseDto getWarning(Long intersectionId) {
        Member member = findByUsername(getCurrentUsername());
        PathIntersection pathIntersection = findPathIntersectionByMemberAndIntersectionId(member, intersectionId, false);
        return MembersWarningGetResponseDto.from(pathIntersection);
    }

    @Transactional
    public void delete() {
        Member member = findByUsername(getCurrentUsername());
        List<Intersection> intersections = getIntersections(member);
        if (intersections.size() > 0) {
            intersectionRepository.deleteAll(intersections);
        }
        memberRepository.delete(member);
    }
}
