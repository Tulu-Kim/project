package com.project.entity;

import com.project.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // cascade = CascadeType.ALL 영속성 전이.
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러 개의 주문 상품을 갖으므로 List 자료형을 사용하여 매핑

    public void addOrderItem(OrderItem orderItem){ // orderItems 에는 주문 상품 정보들을 담는다. orderItem 객체를 osrderItems에 추가함.
        orderItems.add(orderItem);
        orderItem.setOrder(this); //Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계 이므로, orderItem 객체에도 order 객체를 세팅한다.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member); // 상품을 주문한 회원의 정보를 세팅
        for(OrderItem orderItem : orderItemList){ // 상품페이지에서는 한가지만 주문, 장바구니 페이지에서는 한 번에 여러개를 주문할 수 있다.
            // 따라서 여러개의 주문 상품을 담을 수 있도록 리스트형태로 파타미터 값을 받으며 주문객체에 orderItem 객체를 추가한다.
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); //주문 상태를 ORDER 세팅
        order.setOrderDate(LocalDateTime.now()); //현재 시간을 주문 시간으로 세팅
        return order;
    }

    public int getTotalPrice(){//총 주문 금액을 구하는 메소드
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
