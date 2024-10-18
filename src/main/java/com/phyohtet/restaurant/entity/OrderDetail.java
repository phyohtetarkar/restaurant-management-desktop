package com.phyohtet.restaurant.entity;

import jakarta.persistence.Entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

@SuppressWarnings("serial")
@Entity
public class OrderDetail implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private long id;
	private int totalItem;
	private int totalPrice;
	private LocalDate refDate;
	private LocalTime refTime;
	@OneToMany(cascade = { PERSIST, REMOVE, MERGE }, mappedBy = "orderDetail", fetch = EAGER)
	private Set<OrderItem> orderItems;
	@ManyToOne
	private OrderHead orderHead;

	private boolean deleted;

	@PrePersist
	private void prePersist() {
		refDate = LocalDate.now();
		refTime = LocalTime.now();
		orderItems.stream().forEach(oi -> oi.setOrderDetail(this));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDate getRefDate() {
		return refDate;
	}

	public void setRefDate(LocalDate refDate) {
		this.refDate = refDate;
	}

	public String getRefTime() {
		return refTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
	}

	public void setRefTime(LocalTime refTime) {
		this.refTime = refTime;
	}

	public List<OrderItem> getOrderItems() {
		return new ArrayList<>(orderItems);
	}

	public void setOrderItems(Set<OrderItem> orderItem) {
		this.orderItems = orderItem;
	}

	public OrderHead getOrderHead() {
		return orderHead;
	}

	public void setOrderHead(OrderHead orderHead) {
		this.orderHead = orderHead;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
