package com.vroomvroom.safemobis.repository;

import com.vroomvroom.safemobis.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"trafficModes", "position"})
    Optional<Member> findByUsername(String username);

    @Override
    @EntityGraph(attributePaths = {"trafficModes", "position"})
    List<Member> findAll();
}
