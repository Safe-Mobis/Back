package com.vroomvroom.safemobis.repository;

import com.vroomvroom.safemobis.domain.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntersectionRepository extends JpaRepository<Intersection, Long> {
}
