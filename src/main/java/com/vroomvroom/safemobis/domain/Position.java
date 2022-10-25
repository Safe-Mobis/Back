package com.vroomvroom.safemobis.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
