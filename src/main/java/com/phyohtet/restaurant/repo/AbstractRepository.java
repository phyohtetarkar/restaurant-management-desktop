package com.phyohtet.restaurant.repo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public abstract class AbstractRepository<T, ID extends Serializable> {
    private static final String SELECT = "select x from %s x ";
    private static final String COUNT = "select count(x.id) from %s x ";

    @PersistenceContext
    protected EntityManager em;
    private Class<T> type;

    public AbstractRepository(Class<T> type) {
        this.type = type;
    }

    public List<T> select(String where, Map<String, Object> params) {
        return select(null, where, params);
    }

    public List<T> select(String join, String where, Map<String, Object> params) {
        String jpql = String.format(SELECT, type.getSimpleName());
        String del = "x.deleted = FALSE ";
        if (null != join && !join.isEmpty()) {
            jpql = jpql.concat("join ").concat(join);
        }

        if (null != where && !where.isEmpty()) {
            jpql = jpql.concat("where ").concat(String.format("%s and %s ", where, del));
        } else {
            jpql = jpql.concat("where ").concat(del);
        }

        return doSelect(jpql, params);
    }

    private List<T> doSelect(String jpql, Map<String, Object> params) {
        System.out.println(jpql);

        TypedQuery<T> query = em.createQuery(jpql, type);

        if (null != params && !params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.getResultList();
    }

    public int getCount() {
        String jpql = String.format(COUNT, type.getSimpleName());
        TypedQuery<T> query = em.createQuery(jpql, type);
        return query.getFirstResult();
    }

    public T findById(ID id) {
        return em.find(type, id);
    }

    public <S extends T> void create(S t) {
        em.persist(t);
    }

    public void update(T t) {
        em.merge(t);
    }

    public void delete(ID id) {
        em.remove(em.find(type, id));
    }
}
