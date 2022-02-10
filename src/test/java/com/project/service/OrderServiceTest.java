package com.project.service;

import com.project.constant.ItemSellStatus;
import com.project.dto.OrderDto;
import com.project.entity.Item;
import com.project.entity.Member;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.repository.ItemRepository;
import com.project.repository.MemberRepository;
import com.project.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional //트렌젝션 어노테이션 , 이 어노테이션이 붙은 메소드작업중 하나라도 작업이 실패하면 전체 작업을 취소시킨다.
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Item saveItem(){ // 테스트용 상품정보를 저장하는 메소드
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){ // 테스트용 회원정보를 저장하는 메소드
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);

    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10); //상품수량을 orderDto 객체에 저장
        orderDto.setItemId(item.getId()); //위에서 저장한 테스트용 상품Id를 orderDto 객체에 저장

        Long orderId = orderService.order(orderDto, member.getEmail()); // 주문 로직 호출 결과 생성된 주문 번호를 orderId 변수에 저장.
        Order order = orderRepository.findById(orderId) //주문 번호를 이용하여 저장된 주문 정보를 조회.
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getCount()*item.getPrice(); //주문한 상품의 총 가격을 구한다.

        assertEquals(totalPrice, order.getTotalPrice()); //주문한 상품의 총 가격과 데이터베이스에 저장된 상품의 가격을 비교하여 같으면 테스트를 성공.
    }




}
