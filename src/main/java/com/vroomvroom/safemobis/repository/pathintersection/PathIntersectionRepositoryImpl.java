package com.vroomvroom.safemobis.repository.pathintersection;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.PathIntersection;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.vroomvroom.safemobis.domain.QPathIntersection.pathIntersection;

public class PathIntersectionRepositoryImpl implements PathIntersectionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PathIntersectionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<PathIntersection> findByMemberAndIntersectionId(Member member, Long intersectionId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(pathIntersection)
                .join(pathIntersection.path).fetchJoin()
                .join(pathIntersection.intersection).fetchJoin()
                .where(
                        pathIntersection.path.member.eq(member),
                        pathIntersection.intersection.id.eq(intersectionId)
                )
                .fetchOne());
    }

    @Override
    public Optional<PathIntersection> findByMemberNotAndIntersectionId(Member member, Long intersectionId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(pathIntersection)
                .join(pathIntersection.path).fetchJoin()
                .join(pathIntersection.intersection).fetchJoin()
                .where(
                        pathIntersection.path.member.ne(member),
                        pathIntersection.intersection.id.eq(intersectionId)
                )
                .fetchOne());
    }
}
