package com.phyohtet.restaurant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.repo.OrderHeadRepo;
import com.phyohtet.restaurant.util.ValidatorUtil;

@Service
public class OrderHeadService {
	
	@Autowired
	private OrderHeadRepo repo;
	
	public List<OrderHead> findAll() {
		return repo.findByDeletedFalse();
	}

	@Transactional
	public void save(OrderHead t) {
		repo.save(t);
	}

	public void validate(OrderHead t) {
		if (ValidatorUtil.isNullObject(t)) {
			throw new ApplicationException("Please contact to developer");
		}
		
		if (ValidatorUtil.isEmptyString(t.getName())) {
			throw new ApplicationException("OrderHead name must not be empty!");
		}
	}
}
