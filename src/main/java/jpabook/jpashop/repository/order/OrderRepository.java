package jpabook.jpashop.repository.order;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOrder(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * JPQL을 이용한 문자열 방법의 동적쿼리는 다음과 같이 매우 복잡하다.
     * 이는 이후, QueryDSL 등의 기술로 해결한다.
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAll(OrderSearch orderSearch) {

        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        List<Order> resultList = em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class
        ).getResultList(); // Fetch-join 이용 (XToOne 관계에 대해서)

        return resultList;
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        List<Order> resultList = em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class
        )
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList(); // Fetch-join 이용 (XToOne 관계에 대해서)

        return resultList;
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        List<OrderSimpleQueryDto> resultList = em.createQuery("select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
        return resultList;
    }

    public List<Order> findAllWithItem() {
        // distinct JPA에서 같은 id 비교 후 중복을 제거해준다. (디비 SQL에서는 거르지 못함.)
        return em.createQuery("select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i"
                , Order.class).getResultList();

        // 페치 조인을 통해 쿼리가 한번 나간다.
        // 서비스로 페치 조인을 했냐 안했냐의 차이 / 컨트롤러 단 코드는 차이가 없다!
        // 일대다를 페치 조인 하는 순간, 페이징 기능이 사용 불가능해진다.

    }


    /**
     * Criteria를 이용한 동적 쿼리 방법
     *
     * @param orderSearch
     * @return
     */
//    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
//        Root<Order> o = cq.from(Order.class);
//        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
//        List<Predicate> criteria = new ArrayList<>();
////주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            javax.persistence.criteria.Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
//            criteria.add((Predicate) status);
//        }
////회원 이름 검색
//        if (StringUtils.hasText(orderSearch.getMemberName())) {
//            Predicate name =
//                    (Predicate) cb.like(m.<String>get("name"), "%" +
//                            orderSearch.getMemberName() + "%");
//            criteria.add(name);
//
//        }
//        cq.where(cb.and((javax.persistence.criteria.Predicate) criteria.toArray(new Predicate[criteria.size()])));
//        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
//        return query.getResultList();
//    }
}