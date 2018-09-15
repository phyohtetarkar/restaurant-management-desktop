package com.phyohtet.restaurant.util;

import java.util.LinkedList;
import java.util.List;

import com.phyohtet.restaurant.entity.OrderDetail;

public class OrderHolder {

	private static OrderHolder INSTANCE;
	private List<OrderDetail> orderDetails;

	private OrderHolder() {
		orderDetails = new LinkedList<>();
	}

	public static OrderHolder getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new OrderHolder();
		}

		return INSTANCE;
	}

	public void add(OrderDetail orderDetail) {
		orderDetails.add(orderDetail);
	}

	public void replace(OrderDetail orderDetail) {
		int index = orderDetails.indexOf(orderDetail);
		orderDetails.remove(orderDetail);
		orderDetails.add(index, orderDetail);
	}

	public void remove(OrderDetail orderDetail) {
		orderDetails.remove(orderDetail);
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

}
