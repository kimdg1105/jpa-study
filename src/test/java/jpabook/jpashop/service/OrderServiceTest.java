package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.enums.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 테스트 끝나고 롤백 수행
//@Rollback(value = false)
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item item = createItem("Spring", 12000, 10);
        
        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order order = orderRepository.findOrder(orderId);
        assertEquals(OrderStatus.ORDER, order.getStatus(),"상품 주문시 상태는 ORDER");
        assertEquals(1,order.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 함");
        assertEquals(24000,order.getTotalPrice(),"상품 가격 = 갯수 * 금액");
        assertEquals(8,item.getStockQuantity(),"상품 주문시 재고가 줄어들어야 한다.");
    }



    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem("JPA", 11000, 20);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);


        //then
        Order order = orderRepository.findOrder(orderId);

        assertEquals(20,book.getStockQuantity(),"재고가 복구되어야 한다.");
        assertEquals(OrderStatus.CANCEL,order.getStatus(),"취소 상태로 변경되어야 한다.");
    }


    @Test()
    public void 상품주문_재고수량초과() throws NotEnoughStockException {
        //given
        Member member = createMember();
        Item item = createItem("Spring", 12000, 10);

        int orderCount = 11;


        //when

        //then
        Assertions.assertThrows(NotEnoughStockException.class,() -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
//        fail("재고 수량 부족 예외가 발생해야 한다.");

    }



    private Item createItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Donggyu");
        member.setAddress(new Address("Seoul", "mapo", "123-123"));
        em.persist(member);
        return member;
    }
}