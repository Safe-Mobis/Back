package com.vroomvroom.safemobis.repository.pathintersection;

import com.vroomvroom.safemobis.domain.PathIntersection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathIntersectionRepository extends JpaRepository<PathIntersection, Long>, PathIntersectionRepositoryCustom {
}
