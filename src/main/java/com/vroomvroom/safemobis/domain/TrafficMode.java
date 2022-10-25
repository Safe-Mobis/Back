package com.vroomvroom.safemobis.domain;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TrafficMode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrafficCode trafficCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean carFlag;

    @Column(nullable = false)
    private Boolean pedestrianFlag;

    @Column(nullable = false)
    private Boolean childFlag;

    @Column(nullable = false)
    private Boolean kickBoardFlag;

    @Column(nullable = false)
    private Boolean bicycleFlag;

    @Column(nullable = false)
    private Boolean motorcycleFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
