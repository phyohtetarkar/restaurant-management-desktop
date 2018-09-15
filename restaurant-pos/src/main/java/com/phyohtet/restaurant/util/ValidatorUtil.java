package com.phyohtet.restaurant.util;

import java.util.List;
import java.util.Set;

public class ValidatorUtil {

	public static boolean isEmptyString(String text) {
		return (text == null || text.isEmpty());
	}

	public static boolean isWrongNumberFormat(String text) {
		try {
			int num = Integer.parseInt(text);
			if (num > 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return true;
	}
	
	public static boolean isWrongNumberFormat(int num) {
		try {
			if (num > 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return true;
	}

	public static <T> boolean isNullObject(T type) {
		return (type == null);
	}

	public static <T> boolean isEmptyList(List<T> list) {
		return list.isEmpty();
	}

	public static <T> boolean isEmptyList(Set<T> set) {
		return set.isEmpty();
	}
}
