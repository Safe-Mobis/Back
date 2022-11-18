package com.vroomvroom.safemobis.service;

import com.vroomvroom.safemobis.domain.Intersection;
import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Path;
import com.vroomvroom.safemobis.domain.PathIntersection;
import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import com.vroomvroom.safemobis.dto.response.member.MembersIntersectionsGetResponseDto;
import com.vroomvroom.safemobis.dto.response.member.MembersWarningGetResponseDto;
import com.vroomvroom.safemobis.dto.response.member.TokenInfo;
import com.vroomvroom.safemobis.error.exception.EntityAlreadyExistException;
import com.vroomvroom.safemobis.error.exception.EntityNotFoundException;
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

import java.util.ArrayList;
import java.util.List;

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
        PathIntersection pathIntersection = pathIntersectionRepository.findByMemberAndIntersectionId(member, intersectionId)
                .orElseThrow(() -> new EntityNotFoundException("[" + intersectionId + "] 해당 교차지점에 대한 경로가 없습니다."));
        pathIntersection.setWarning(warningCode, warningPosition);
    }

    public MembersWarningGetResponseDto getWarning(Long intersectionId) {
        Member member = findByUsername(getCurrentUsername());
        PathIntersection pathIntersection = pathIntersectionRepository.findByMemberNotAndIntersectionId(member, intersectionId)
                .orElseThrow(() -> new EntityNotFoundException("[" + intersectionId + "] 해당 교차지점에 대한 경로가 없습니다."));
        return MembersWarningGetResponseDto.from(pathIntersection);
    }
}
