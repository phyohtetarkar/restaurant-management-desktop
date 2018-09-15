package com.phyohtet.restaurant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phyohtet.restaurant.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

	List<Category> findByDeletedFalse();
}
