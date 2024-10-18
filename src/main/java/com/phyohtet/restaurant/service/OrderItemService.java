package com.phyohtet.restaurant.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.OrderItem;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.repo.OrderItemRepo;

@Service
public class OrderItemService {
	
	@Autowired
	private OrderItemRepo repo;
	
	public List<OrderItem> findBy(OrderHead sOrderHead, Category sCategory, Type sType, LocalDate sDate, String sName) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder where = new StringBuilder();

		if (null != sOrderHead) {
			where.append("x.orderDetail.orderHead = :orderHead ");
			params.put("orderHead", sOrderHead);
		}
		
		if (null != sCategory) {
			if (!where.toString().isEmpty()) {
				where.append("and ");
			}
			where.append("x.item.category = :category ");
			params.put("category", sCategory);
		}
		
		if (null != sDate) {
			if (!where.toString().isEmpty()) {
				where.append("and ");
			}
			where.append("x.orderDetail.refDate = :refDate ");
			params.put("refDate", sDate);
		}

		if (null != sName && !sName.isEmpty()) {
			if (!where.toString().isEmpty()) {
				where.append("and ");
			}
			where.append("upper(x.name) like upper(:name) ");
			params.put("name", "%" + sName + "%");
		}
		
		return repo.select(where.toString(), params);
	}

	@Transactional
	public void create(OrderItem t) {
		repo.create(t);
	}

	@Transactional
	public void update(OrderItem t) {
		repo.update(t);
	}

	@Transactional
	public void delete(OrderItem t) {
		repo.update(t);
	}
	
}
