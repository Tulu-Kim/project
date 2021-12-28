package com.project.repository;

import com.project.dto.ItemSearchDto;
import com.project.dto.MainItemDto;
import com.project.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    // 267p 1. 상품 조회 조건을 담고 있는 itemSearchDto 객체와 페이징 정보를 담고 있는 pageable 객체를 파라미터로 받는
    // getAdminItemPage 메소드. 반환 데이터로 Page<Item> 객체를 반환

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
