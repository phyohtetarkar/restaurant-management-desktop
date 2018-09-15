package com.phyohtet.restaurant.repo;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.phyohtet.restaurant.entity.OrderItem;

@Repository
public class SummaryRepo {

	@PersistenceContext
	private EntityManager em;
	
	public List<Object[]> selectColumns(String columns, String from, String where, Map<String, Object> params) {
		String className = OrderItem.class.getSimpleName();
		StringBuilder jpql = new StringBuilder("select ");
		
		jpql.append(columns != null ? columns : "x ");
		jpql.append(from != null ? "from ".concat(from) : "from ".concat(className.concat(" x ")));
		jpql.append(where != null && !where.isEmpty() ? "where ".concat(where) : "");

		return doSelect(jpql.toString(), params);
	}
	
	private List<Object[]> doSelect(String jpql, Map<String, Object> params) {
		System.out.println(jpql);
		
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

		if (null != params && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		
		return query.getResultList();
	}
}
