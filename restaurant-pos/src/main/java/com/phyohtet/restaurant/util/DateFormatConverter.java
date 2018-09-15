package com.phyohtet.restaurant.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateFormatConverter extends StringConverter<LocalDate> {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public String toString(LocalDate date) {
		return date != null ? formatter.format(date) : null;
	}

	@Override
	public LocalDate fromString(String date) {
		return LocalDate.parse(date, formatter);
	}
}
