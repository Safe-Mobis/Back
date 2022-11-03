package com.vroomvroom.safemobis.domain;

import lombok.*;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static java.lang.Math.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double direction;

    @Column(nullable = false)
    private double velocity;

    @Column(nullable = false)
    private double acceleration;

    @Column
    private Point point;

    @Column
    private LineString lineString;

    @OneToOne(mappedBy = "position")
    private Member member;

    public void updatePosition(Position updatePosition) {
        x = updatePosition.getX();
        y = updatePosition.getY();
        direction = updatePosition.getDirection();
        velocity = updatePosition.getVelocity();
        acceleration = updatePosition.getAcceleration();
    }

    public Position getPositionAfter(double time) {
        double space = velocity * time + 0.5 * acceleration * pow(time, 2);
        double x_prime = x + space * cos(direction);
        double y_prime = y + space * sin(direction);
        return Position.builder()
                .x(x_prime)
                .y(y_prime)
                .direction(direction)
                .velocity(velocity)
                .acceleration(acceleration)
                .build();
    }
}
