package jpabook.jpashop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.*;

//@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
//		SpringApplication.run(JpashopApplication.class, args);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member1 = new Member(1L,"Tester1");
			Member member2 = new Member(2L,"Tester2");

			em.persist(member1);
			em.persist(member2);

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		}
		finally {
			em.close();
		}

		emf.close();

	}

}
