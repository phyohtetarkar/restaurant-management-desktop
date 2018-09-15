package com.phyohtet.restaurant.view.custom;

import java.util.function.Consumer;

import com.phyohtet.restaurant.entity.Item;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ItemView extends VBox {

	public ItemView(Item item, Consumer<Item> handler) {
		
		getStyleClass().addAll("item-box", "color-700", "shadowSolid", "cursor-hand");

		Label name = new Label();
		name.setText(item.getName());
		getChildren().add(name);
		if (null != item.getType()) {
			Label type = new Label();
			type.setText("(" + item.getType().getName() + ")");
			getChildren().add(type);
		}
		
		Label price = new Label();
		price.setText(String.valueOf(item.getPrice()));
		getChildren().add(price);

		setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				handler.accept(item);
			}
		});
	}
}
