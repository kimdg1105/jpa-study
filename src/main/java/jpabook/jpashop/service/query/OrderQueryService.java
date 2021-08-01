package jpabook.jpashop.service.query;


import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.enums.OrderStatus;
import jpabook.jpashop.repository.order.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> ordersV3(){

        List<Order> orderList = orderRepository.findAllWithItem();

        List<OrderDto> collect = orderList.stream()
                .map(order -> new OrderDto(order))
                .collect(toList());
        return collect;
    }

    @Getter
    public static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderApiController.OrderItemDto> orderItems;


        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderStatus = order.getStatus();
            orderDate = order.getOrderDate();
            address = order.getDelivery().getAddress();
//            order.getOrderItems().stream()
//                    .forEach(orderItem -> orderItem.getItem().getName());
//            // DTO 안에 엔티티가 있는 것 또한 부적합하다. 엔티티에 대한 의존을 완전히 제거해야 한다.
            List<OrderApiController.OrderItemDto> list = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderApiController.OrderItemDto orderItemDto = new OrderApiController.OrderItemDto(orderItem);
                list.add(orderItemDto);
            }
            orderItems = list;
        }
    }

}
