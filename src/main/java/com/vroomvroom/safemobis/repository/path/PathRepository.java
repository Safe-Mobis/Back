package com.vroomvroom.safemobis.repository.path;

import com.vroomvroom.safemobis.domain.Member;
import com.vroomvroom.safemobis.domain.Path;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PathRepository extends JpaRepository<Path, Long> {

    @EntityGraph(attributePaths = {"pathIntersections", "pathIntersections.intersection"})
    @Query("select distinct p from Path p where p.member = :member")
    List<Path> findByMemberWithIntersection(@Param("member") Member member);

}
