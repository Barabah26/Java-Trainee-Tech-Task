package com.example.javatraineetechtask.repository;

import com.example.javatraineetechtask.dto.MemberDto;
import com.example.javatraineetechtask.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
}
