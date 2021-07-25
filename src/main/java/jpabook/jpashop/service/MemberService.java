package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service // 컴포넌트 스캔 대상
@Transactional(readOnly = true) // 이 옵션을 주면, 조회 기능에 대해서는 성능 최적화를 한다.
//@AllArgsConstructor
@RequiredArgsConstructor // final이 있는 것에 대해서만 생성자를 만들어준다.
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired // 세터 인젝션
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 조회 비지니스 로직
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        //Exception
    }

    // 회원 전체 조회
//    @Transactional(readOnly = true) // 이 옵션을 주면, 조회 기능에 대해서는 성능 최적화를 한다.
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOneMember(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
