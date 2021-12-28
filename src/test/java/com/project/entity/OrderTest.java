package com.project.entity;

import com.project.constant.ItemSellStatus;
import com.project.repository.ItemRepository;
import com.project.repository.MemberRepository;
import com.project.repository.OrderItemRepository;
import com.project.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class OrderTest { // 주문 엔티티를 저장할 때 영속성 전이가 일어나는지에 대한 테스트 코드

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){

        Order order = new Order();

        for(int i=0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); //1.
        }

        orderRepository.saveAndFlush(order); // 2.
        em.clear(); // 3.

        Order savedOrder = orderRepository.findById(order.getId()) // 4.
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());

    }
    public Order createOrder(){
        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트") // 실제로는 지연로딩테스트 전에 즉시로딩이 어떤건지인지 테스트 하기위한 것.
    public void lazyLoadinTest(){
        Order order = this.createOrder(); // 214p 1. 기존에 만들었던 주문 생성 메소드를 이용하여 주문 데이터를 저장
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                // 214p 2. 영속성 컨텍스트의 상태 초기화 후 order엔티티에 저장했던 주문 상품 아이디를 이용하여 orderItem을
                // 데이터 베이스에서 다시 조회한다.
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : "+ orderItem.getOrder()
                .getClass()); // 214p 3. orderItem 엔티티에 있는 order 객체의 클래스를 출력합니다. Order 클래스가 출력되는것을
                                // 확인할 수 있음. 출력 결과 : Order class : class com.shop.entity.Order

        System.out.println("========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("========================");

    }

}
