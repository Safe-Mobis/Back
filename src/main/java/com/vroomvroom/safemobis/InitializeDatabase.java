package com.vroomvroom.safemobis;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Position;
import com.vroomvroom.safemobis.service.MemberService;
import lombok.RequiredArgsConstructor;
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
        return Position.builder()
                .x(0.00)
                .y(0.0)
                .direction(0.0)
                .velocity(0.0)
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
        return Position.builder()
                .x(0.025)
                .y(0.025)
                .direction(0.0)
                .velocity(0.0)
                .build();
    }

}
