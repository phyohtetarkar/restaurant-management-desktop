package com.phyohtet.restaurant.view.util;

import java.lang.reflect.Field;
import java.util.List;

import com.phyohtet.restaurant.entity.Item;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class TableArrayCell<T> extends TableCell<Item, List<T>> {

	@Override
	protected void updateItem(List<T> list, boolean empty) {
		super.updateItem(list, empty);

		if (list != null && !empty) {
			HBox hbox = new HBox(5);
			hbox.setAlignment(Pos.CENTER);
			hbox.getStyleClass().addAll("space5", "padd5");

			list.stream().forEach(t -> {

				try {
					Field f = t.getClass().getDeclaredField("name");
					f.setAccessible(true);
					hbox.getChildren().add(new Chip((String) f.get(t)));
				} catch (Exception e) {
					e.printStackTrace();
				}

			});

			setGraphic(hbox);

		} else {
			setGraphic(null);
		}
	}
}
