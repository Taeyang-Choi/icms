package com.ogp.icms.member.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.ogp.icms.global.util.MD5;
import com.ogp.icms.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 유저 아이디로 회원 찾기
    Member findByUserid(String userid);

    // 등급으로 회원 찾기
    List<Member> findByGrade(String grade);

    List<Member> findByTeam(String teamCode);

    Member getByUserid(String userid);
}
