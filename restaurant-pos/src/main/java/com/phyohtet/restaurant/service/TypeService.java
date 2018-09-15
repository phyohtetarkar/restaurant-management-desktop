package com.phyohtet.restaurant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.repo.TypeRepo;
import com.phyohtet.restaurant.util.ValidatorUtil;

@Service
public class TypeService {
	
	@Autowired
	private TypeRepo repo;
	
	public List<Type> findAll() {
		return repo.findByDeletedFalse();
	}

	@Transactional
	public void save(Type t) {
		repo.save(t);
	}

	public void validate(Type t) {
		if (ValidatorUtil.isNullObject(t)) {
			throw new ApplicationException("Please contact to developer");
		}
		
		if (ValidatorUtil.isEmptyString(t.getName())) {
			throw new ApplicationException("Type name must not be empty!");
		}
	}
}
