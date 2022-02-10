package com.project.service;

import com.project.dto.OrderDto;
import com.project.entity.Item;
import com.project.entity.Member;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.repository.ItemRepository;
import com.project.repository.MemberRepository;
import com.project.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional //트렌젝션 어노테이션 , 이 어노테이션이 붙은 메소드작업중 하나라도 작업이 실패하면 전체 작업을 취소시킨다.
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityExistsException::new); // ::new 람다식으로 이것은 생성자 참조 표현식이라고도 합니다.
        // 주문할 상품을 조회
    Member member = memberRepository.findByEmail(email); //현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 조회

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 엔티티를 생성
    orderItemList.add(orderItem);

    Order order = Order.createOrder(member, orderItemList); //회원정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
    orderRepository.save(order); // 생성한 주문 엔티티를 저장.

    return order.getId();
    }
}
