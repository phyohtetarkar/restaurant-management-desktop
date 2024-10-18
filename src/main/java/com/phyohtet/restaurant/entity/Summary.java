package com.phyohtet.restaurant.entity;

import java.io.Serializable;

import javafx.scene.chart.XYChart.Data;

@SuppressWarnings("serial")
public class Summary implements Serializable {

	private String name;
	private Number value;

	public Summary(String name, Number value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public Data<String, Number> getData() {
		return new Data<>(name, value == null ? 0 : value);
	}
}
