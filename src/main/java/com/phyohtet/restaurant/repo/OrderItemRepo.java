package com.phyohtet.restaurant.repo;

import org.springframework.stereotype.Repository;

import com.phyohtet.restaurant.entity.OrderItem;

@Repository
public class OrderItemRepo extends AbstractRepository<OrderItem, Long> {

	public OrderItemRepo() {
		super(OrderItem.class);
	}

}
