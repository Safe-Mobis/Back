package com.vroomvroom.safemobis.domain;

import lombok.*;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Path extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "GEOMETRY SRID 4326")
    private LineString route;

    @Column(columnDefinition = "GEOMETRY SRID 4326")
    private Point warningPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PathIntersection> pathIntersections = new ArrayList<>();

    public void setPathIntersections(List<PathIntersection> pathIntersections) {
        this.pathIntersections = pathIntersections;
    }
}
