package com.phyohtet.restaurant.view.util;

import java.lang.reflect.Field;

import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class TableNameCell<T, S> extends TableCell<T, S> {

	@Override
	protected void updateItem(S item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null && !empty) {

			try {
				String value = "";
				if (item instanceof String) {
					value = (String) item;
				} else {
					Field f = item.getClass().getDeclaredField("name");
					f.setAccessible(true);
					value = (String) f.get(item);
				}

				Text text = new Text();

				setPrefHeight(Control.USE_COMPUTED_SIZE);
				text.wrappingWidthProperty().bind(widthProperty().subtract(35));
				text.textProperty().setValue(value);
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
