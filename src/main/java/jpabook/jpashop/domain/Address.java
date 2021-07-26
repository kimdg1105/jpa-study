package jpabook.jpashop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@Getter // getter만 만든 이유? -> 생성할 때만 값을 넣고, 값의 중간 수정이 불가능하게 설계
public class Address {
    private String city;
    private String street;
    private String zipcode;

    public Address() {
    }


    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
