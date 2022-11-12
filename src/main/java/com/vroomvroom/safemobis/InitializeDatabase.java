package com.vroomvroom.safemobis;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.CAR;

//@Component
@Transactional
@RequiredArgsConstructor
public class InitializeDatabase {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private final MemberService memberService;

    @PostConstruct
    public void initialize() throws ParseException {
        if (!ddlAuto.equals("create")) {
            return;
        }
        memberService.save(createTestMember());
        for (int i = 0; i < 1000; i++) {
            memberService.save(createTestSurroundingMember(i));
        }
    }

    private Member createTestMember() throws ParseException {
        return Member.builder()
                .username("han")
                .password("1234")
                .trafficCode(CAR)
                .position(createTestPosition())
                .roles(Collections.singletonList("USER"))
                .build();
    }

    private Position createTestPosition() throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(0.0, 0.0));

        LineString lineString = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(1.0, 1.0),
                new Coordinate(3.0, 3.0)
        });

        LineString lineString2 = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(1.0, 0.0),
                new Coordinate(2.0, 0.0)
        });
        System.out.println("lineString2 = " + lineString.intersects(lineString2));
        Geometry intersection = lineString.intersection(lineString2);
        System.out.println("intersection = " + intersection.getNumPoints());
        LineString line = (LineString) intersection;
        for (int i = 0; i < intersection.getNumPoints(); i++) {
            Point pointN = line.getPointN(i);
            System.out.println("pointN.getY() = " + pointN.getY());
        }
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

    private Member createTestSurroundingMember(int i) {
        return Member.builder()
                .username("han" + i)
                .password("1234")
                .trafficCode(CAR)
                .position(createTestSurroundingPosition())
                .roles(Collections.singletonList("USER"))
                .build();
    }

    private Position createTestSurroundingPosition() {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
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
