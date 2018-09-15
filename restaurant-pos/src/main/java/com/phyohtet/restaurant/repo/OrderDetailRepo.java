package com.phyohtet.restaurant.repo;

import org.springframework.stereotype.Repository;

import com.phyohtet.restaurant.entity.OrderDetail;

@Repository
public class OrderDetailRepo extends AbstractRepository<OrderDetail, Long> {

	public OrderDetailRepo() {
		super(OrderDetail.class);
	}

}
