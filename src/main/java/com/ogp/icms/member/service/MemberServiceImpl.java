package com.ogp.icms.member.service;

import com.ogp.icms.global.util.MD5;
import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.member.dao.MemberJpaRepository;
import com.ogp.icms.member.dao.MemberRepository;
import com.ogp.icms.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class MemberServiceImpl {
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 로그인
     * @// TODO: 2022-07-01 세션 정비 후 여기서 세션 관리해야함...
     */
    public Member login(String id, String pass) {
        pass = MD5.get(pass+"icms");

        Member dbMember = memberRepository.findByUserid(id);
        if(dbMember == null) return null;

        if(dbMember.getUserpwd().equals(pass)) {
            return dbMember;
        }
        else {
            return null;
        }
    }

    public List<Member> getListByGrade(String grade) {
        return memberRepository.findByGrade(grade);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * 멤버 추가
     */
    public ResultCode save(Member member) {
        member.setUserpwd(MD5.get(member.getUserpwd()+"icms"));
        memberRepository.save(member);
        return new ResultCode(0, "멤버를 추가했습니다.");
    }


    /**
     * 멤버 수정
     * @return
     */
    public ResultCode modify(Long id, Member newMember) {
        Member savedMember = memberRepository.findById(id).get();

        if(newMember.getUserpwd().length() > 0) savedMember.setUserpwd(MD5.get(newMember.getUserpwd()+"icms"));

        savedMember.setName(newMember.getName());
        savedMember.setHp(newMember.getHp());
        savedMember.setMonitor(newMember.getMonitor());
        savedMember.setGrade(newMember.getGrade());
        savedMember.setDept(newMember.getDept());
        savedMember.setTeam(newMember.getTeam());

        memberRepository.flush();

        return new ResultCode(0, newMember.getName() + " 멤버를 수정했습니다.");
    }

    public ResultCode deleteMemberById(Long id) {
        memberRepository.deleteById(id);
        return new ResultCode(0, "멤버를 삭제했습니다.");
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }

    public Member getByUserid(String userid) {
        return memberRepository.getByUserid(userid);
    }
}
