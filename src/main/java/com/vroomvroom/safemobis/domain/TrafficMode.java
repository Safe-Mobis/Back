package com.vroomvroom.safemobis.domain;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TrafficMode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrafficCode trafficCode;

    @Column(nullable = false)
    private boolean carFlag;

    @Column(nullable = false)
    private boolean pedestrianFlag;

    @Column(nullable = false)
    private boolean childFlag;

    @Column(nullable = false)
    private boolean kickBoardFlag;

    @Column(nullable = false)
    private boolean bicycleFlag;

    @Column(nullable = false)
    private boolean motorcycleFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
