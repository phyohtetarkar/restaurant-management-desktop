package com.phyohtet.restaurant.repo;

import org.springframework.stereotype.Repository;

import com.phyohtet.restaurant.entity.Item;

@Repository
public class ItemRepo extends AbstractRepository<Item, Integer> {

	public ItemRepo() {
		super(Item.class);
	}
	
}
