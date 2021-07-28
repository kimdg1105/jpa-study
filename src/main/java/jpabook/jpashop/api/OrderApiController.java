package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.enums.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderV1() {
        List<Order> orderList = orderRepository.findAll(new OrderSearch());
        for (Order order : orderList) {
            order.getMember().getName(); // 강제 초기화
            order.getDelivery().getAddress(); // 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName());
        }
        return orderList;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        List<Order> orderList = orderRepository.findAll(new OrderSearch());
        List<OrderDto> collect = orderList.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        List<Order> orderList = orderRepository.findAllWithItem();
        List<OrderDto> collect = orderList.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());

        return collect;
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderStatus = order.getStatus();
            orderDate = order.getOrderDate();
            address = order.getDelivery().getAddress();
//            order.getOrderItems().stream()
//                    .forEach(orderItem -> orderItem.getItem().getName());
//            // DTO 안에 엔티티가 있는 것 또한 부적합하다. 엔티티에 대한 의존을 완전히 제거해야 한다.
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto{

        private String itemName; // 상품명
        private int orderPrice; // 주문 가격
        private int count; // 주문 수

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getTotalPrice();
            this.count = orderItem.getCount();
        }
    }

}
