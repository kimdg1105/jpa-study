package jpabook.jpashop;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext // JPA -> Entity 매니저가 필요
    private EntityManager em;

    public Long save(Member member){
        //영속 상태 확인
        System.out.println("===Before===MemberRepository.save");
        em.persist(member);
        System.out.println("===After===MemberRepository.save");
        return member.getId(); // 커맨드와 커리를 분리하라의 원칙을 따른 케이스
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }


    public List<Member> update(){
        List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();
        return resultList;
    }
}
