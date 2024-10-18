package com.phyohtet.restaurant.service;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.Summary;
import com.phyohtet.restaurant.repo.SummaryRepo;

@Service
public class SummaryService {
	private static final String SELECT_TABLE = "OrderItem x ";
	private static final String SELECT_COLUMN_CATEGORY = "x.item.category, sum(x.quantity) ";
	private static final String SELECT_COLUMN_DATE = "x.orderDetail.refDate, sum(x.quantity) ";
	private static final String SELECT_COLUMN_MONTH = "month(x.orderDetail.refDate), sum(x.quantity) ";
	private static final String GROUP_MONTH = "group by month(x.orderDetail.refDate) order by month(x.orderDetail.refDate) ";
	private static final String GROUP_DATE = "group by x.orderDetail.refDate order by x.orderDetail.refDate ";
	
	@Autowired
	private SummaryRepo repo;

	public List<Summary> getSummaryBy(Object obj, Year year, Month month, boolean monthly) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder where = new StringBuilder();

		if (obj instanceof Item) {
			where.append("x.item.id = :id ");
			params.put("id", ((Item) obj).getId());
		} else if (obj instanceof Category) {
			where.append("x.item.category.id = :id ");
			params.put("id", ((Category) obj).getId());
		} else if (obj instanceof OrderHead) {
			where.append("x.orderDetail.orderHead.id = :id ");
			params.put("id", ((OrderHead) obj).getId());
		} else {
			List<Object[]> list = repo.selectColumns(SELECT_COLUMN_CATEGORY, 
					SELECT_TABLE, null, null);
			return convertToSummary(list, monthly);
		}
		
		if (null != year) {
			where.append("and year(x.orderDetail.refDate) = :year ");
			params.put("year", year.getValue());
		}
		
		if (null != month) {
			where.append("and month(x.orderDetail.refDate) = :month ");
			params.put("month", month.getValue());
		} else {
			monthly = true;
		}
		
		if (monthly) {
			where.append(GROUP_MONTH);
		} else {
			where.append(GROUP_DATE);
		}
		
		List<Object[]> list = repo.selectColumns(monthly ? SELECT_COLUMN_MONTH : SELECT_COLUMN_DATE, SELECT_TABLE
				, where.toString(), params);
		
		return convertToSummary(list, monthly);
	}

	private List<Summary> convertToSummary(List<Object[]> list, boolean monthly) {
		List<Summary> summaries = new ArrayList<>();
		list.stream().forEach(o -> {
			summaries.add(new Summary(monthly ? Month.of((int) o[0]).toString() : o[0].toString(), (Number) o[1]));
		});
		return summaries;
	}
}
