package com.vroomvroom.safemobis.domain;

import com.vroomvroom.safemobis.domain.enumerate.WarningCode;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PathIntersection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_intersection_id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarningCode warningCode;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point warningPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id")
    private Path path;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    public void setWarning(WarningCode warningCode, Point warningPosition) {
        this.warningCode = warningCode;
        this.warningPosition = warningPosition;
    }
}
