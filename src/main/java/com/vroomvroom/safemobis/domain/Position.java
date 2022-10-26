package com.vroomvroom.safemobis.domain;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double direction;

    @Column(nullable = false)
    private Double velocity;

    @OneToOne(mappedBy = "position")
    private Member member;

    public void updatePosition(Position updatePosition) {
        x = updatePosition.getX();
        y = updatePosition.getY();
        direction = updatePosition.getDirection();
        velocity = updatePosition.getVelocity();
    }
}
