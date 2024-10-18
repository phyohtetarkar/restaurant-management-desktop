package com.phyohtet.restaurant.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phyohtet.restaurant.entity.OrderDetail;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.repo.OrderDetailRepo;

@Service
public class OrderDetailService {
	
	@Autowired
	private OrderDetailRepo repo;
	
	public List<OrderDetail> findBy(OrderHead sOrderHead, LocalDate sDate) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder where = new StringBuilder();

		if (null != sOrderHead) {
			where.append("x.orderHead = :orderHead ");
			params.put("orderHead", sOrderHead);
		}
		
		if (null != sDate) {
			if (!where.toString().isEmpty()) {
				where.append("and ");
			}
			where.append("x.refDate = :refDate ");
			params.put("refDate", sDate);
		}
		
		return repo.select(where.toString(), params);
	}

	@Transactional
	public void create(OrderDetail t) {
		repo.create(t);
	}

	@Transactional
	public void update(OrderDetail t) {
		repo.update(t);
	}

	@Transactional
	public void delete(OrderDetail t) {
		repo.update(t);
	}

}
