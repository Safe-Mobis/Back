package com.vroomvroom.safemobis.repository.pathintersection;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.PathIntersection;

import java.util.Optional;

public interface PathIntersectionRepositoryCustom {

    Optional<PathIntersection> findByMemberAndIntersectionId(Member member, Long intersectionId);

    Optional<PathIntersection> findByMemberNotAndIntersectionId(Member member, Long intersectionId);
}
