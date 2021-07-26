package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void  업데이트_테스트() throws Exception{
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("asd");

        // 변경 감지 == Dirty checking

        //TX
    }
}
