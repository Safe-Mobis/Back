package com.vroomvroom.safemobis;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
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
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @PostConstruct
    public void initialize() {
        if (!ddlAuto.equals("create")) {
            return;
        }
        memberService.save(createTestMember());
        memberService.save(createTestSurroundingMember());
    }

    private Member createTestMember() {
        return Member.builder()
                .username("han")
                .password("1234")
                .trafficCode(CAR)
                .position(createTestPosition())
                .roles(Collections.singletonList("USER"))
                .build();
    }

    private Position createTestPosition() {
        Point point = geometryFactory.createPoint(new Coordinate(2.0, 3.0));
        LineString lineString = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(2.0, 3.0),
                new Coordinate(3.0, 4.0)
        });
        return Position.builder()
                .x(0.0)
                .y(0.0)
                .direction(0.0)
                .velocity(0.0)
                .acceleration(0.0)
                .point(point)
                .lineString(lineString)
                .build();
    }

    private Member createTestSurroundingMember() {
        return Member.builder()
                .username("han2")
                .password("1234")
                .trafficCode(CAR)
                .position(createTestSurroundingPosition())
                .roles(Collections.singletonList("USER"))
                .build();
    }

    private Position createTestSurroundingPosition() {
        Point point = geometryFactory.createPoint(new Coordinate(2.0, 3.0));
        LineString lineString = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(2.0, 3.0),
                new Coordinate(3.0, 4.0)
        });
        return Position.builder()
                .x(0.0)
                .y(0.0)
                .direction(0.0)
                .velocity(0.0)
                .acceleration(0.0)
                .point(point)
                .lineString(lineString)
                .build();
    }

}
