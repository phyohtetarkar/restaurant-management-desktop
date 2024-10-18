package com.phyohtet.restaurant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.repo.CategoryRepo;
import com.phyohtet.restaurant.util.ValidatorUtil;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepo repo;
	
	public List<Category> findAll() {
		return repo.findByDeletedFalse();
	}
	
	public Category findById(int id) {
		return repo.findById(id).orElse(null);
	}

	@Transactional
	public void save(Category t) {
		//validate(t);
		repo.save(t);
	}

	@Transactional
	public void delete(Category t) {
		repo.delete(t);
	}

	public void validate(Category t) {
		if (ValidatorUtil.isNullObject(t)) {
			throw new ApplicationException("Please contact to developer");
		}
		
		if (ValidatorUtil.isEmptyString(t.getName())) {
			throw new ApplicationException("Category name must not be empty!");
		}
	}
}
