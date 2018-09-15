package com.phyohtet.restaurant.entity;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

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
