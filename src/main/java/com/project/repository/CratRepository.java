package com.project.repository;

import com.project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CratRepository extends JpaRepository<Cart, Long> {
}
