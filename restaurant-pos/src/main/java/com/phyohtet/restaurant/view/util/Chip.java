package com.phyohtet.restaurant.view.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class Chip extends Label {

	public Chip(String text) {
		setAlignment(Pos.CENTER);
		setText(text);
		setPadding(new Insets(5, 10, 5, 10));
		getStyleClass().add("custom-chip");
	}
}
