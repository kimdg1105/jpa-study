package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class MemberSampleTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void cacheTest() throws Exception{

        // 비영속
        Member member = new Member();
        member.setUsername("Tester1");

        // 영속 상태
        Long saveId = memberRepository.save(member);

        //find ->
        Member findMember1 = memberRepository.find(1L);
        Member findMember2 = memberRepository.find(1L);
        System.out.println("findMember1 =  findMember2 " + (findMember1 == findMember2)); // 영속 엔티티의 동일성 보장 (같은 트랙잭션 안에서)



    }


}