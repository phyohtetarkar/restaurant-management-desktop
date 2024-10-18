package com.phyohtet.restaurant.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.repo.ItemRepo;
import com.phyohtet.restaurant.util.ValidatorUtil;

@Service
public class ItemService {

	@Autowired
	private ItemRepo repo;

	public List<Item> findAll() {
		return findBy(null, null, null);
	}

	public List<Item> findBy(Category category, Type type, String name) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder where = new StringBuilder();

		if (null != category) {
			where.append("x.category.id = :categoryId ");
			params.put("categoryId", category.getId());
		}

		if (null != type) {
			if (!where.toString().isEmpty()) {
				where.append("and ");
			}
			where.append("x.type.id = :typeId ");
			params.put("typeId", type.getId());
		}

		if (null != name && !name.isEmpty()) {
			if (!where.toString().isEmpty()) {
				where.append("and ");
			}
			where.append("upper(x.name) like upper(:name) ");
			params.put("name", "%" + name + "%");
		}

		return repo.select(where.toString(), params);
	}

	@Transactional
	public void create(Item t) {
		validate(t);
		repo.create(t);
	}

	@Transactional
	public void update(Item t) {
		validate(t);
		repo.update(t);
	}

	@Transactional
	public void delete(Item t) {
		repo.update(t);
	}

	public void validate(Item item) {
		if (ValidatorUtil.isNullObject(item)) {
			throw new ApplicationException("Please contact to developer!");
		}

		if (ValidatorUtil.isNullObject(item.getCategory())) {
			throw new ApplicationException("Please select category!");
		}

		if (ValidatorUtil.isEmptyString(item.getName())) {
			throw new ApplicationException("Please enter item name!");
		}

		if (ValidatorUtil.isWrongNumberFormat(item.getPrice())) {
			throw new ApplicationException("Price must be greater than 0!");
		}

		if (ValidatorUtil.isWrongNumberFormat(item.getTax())) {
			throw new ApplicationException("Tax must be greater than 0!");
		}
	}
}
