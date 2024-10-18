package com.phyohtet.restaurant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phyohtet.restaurant.entity.OrderHead;

public interface OrderHeadRepo extends JpaRepository<OrderHead, Integer> {
	
	List<OrderHead> findByDeletedFalse();
}
