package jpabook.jpashop.repository.order.query;


import lombok.Data;

@Data
public class OrderItemQueryDto {

    private Long orderId;
    private String itemName;
    private int OrderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {

        this.orderId = orderId;
        this.itemName = itemName;
        OrderPrice = orderPrice;
        this.count = count;
    }
}
