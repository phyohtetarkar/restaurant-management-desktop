package com.phyohtet.restaurant.view.util;

import java.lang.reflect.Field;

import javafx.scene.control.ListCell;

public class ListNameCell<T> extends ListCell<T> {

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (!empty) {

			try {
				Field f = item.getClass().getDeclaredField("name");
				f.setAccessible(true);
				
				setText((String) f.get(item));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			setText(null);
		}

	}
	
}
