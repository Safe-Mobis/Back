package com.vroomvroom.safemobis;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Path;
import com.vroomvroom.safemobis.repository.PathRepository;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.CAR;

@Component
@Transactional
@RequiredArgsConstructor
public class InitializeDatabase {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private final MemberService memberService;
    private final PathRepository pathRepository;

    @PostConstruct
    public void initialize() {
        if (!ddlAuto.equals("create")) {
            return;
        }
        Member member = createTestMember();
        memberService.save(member);
        Member member2 = createTestMember2();
        memberService.save(member2);
        pathRepository.save(createTestPath(member2));
    }

    private Member createTestMember() {
        return Member.builder()
                .username("han")
                .password("1234")
                .trafficCode(CAR)
                .roles(Collections.singletonList("USER"))
                .build();
    }

    private Member createTestMember2() {
        return Member.builder()
                .username("han2")
                .password("1234")
                .trafficCode(CAR)
                .roles(Collections.singletonList("USER"))
                .build();
    }

    private Path createTestPath(Member member) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        LineString lineString = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 2.0)
        });
        return Path.builder()
                .route(lineString)
                .member(member)
                .build();
    }

}
