package com.phyohtet.restaurant.view.util;

import java.util.function.Function;

import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class TableCompoundNameCell<T, S> extends TableCell<T, S> {

	private Function<Integer, String> func;

	public TableCompoundNameCell(Function<Integer, String> func) {
		this.func = func;
	}

	@Override
	protected void updateItem(S item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null && !empty) {

			try {
				Text text = new Text();

				setPrefHeight(Control.USE_COMPUTED_SIZE);
				text.wrappingWidthProperty().bind(widthProperty().subtract(35));
				text.textProperty().setValue(func.apply(getIndex()));
				setGraphic(text);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			setGraphic(null);
			setText(null);
		}
	}

}
