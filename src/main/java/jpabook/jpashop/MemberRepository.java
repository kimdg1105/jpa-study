package jpabook.jpashop;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext // JPA -> Entity 매니저가 필요
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId(); // 커맨드와 커리를 분리하라의 원칙을 따른 케이스
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
