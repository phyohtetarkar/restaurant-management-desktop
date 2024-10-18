package com.phyohtet.restaurant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phyohtet.restaurant.entity.Type;

public interface TypeRepo extends JpaRepository<Type, Integer> {

	List<Type> findByDeletedFalse();
}
