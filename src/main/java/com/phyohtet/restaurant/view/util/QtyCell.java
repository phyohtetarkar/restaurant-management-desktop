package com.phyohtet.restaurant.view.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.phyohtet.restaurant.entity.OrderItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class QtyCell extends TableCell<OrderItem, Integer> {

	private BiConsumer<Consumer<OrderItem>, Integer> handler;

	public QtyCell(BiConsumer<Consumer<OrderItem>, Integer> handler) {
		this.handler = handler;
	}

	@Override
	protected void updateItem(Integer item, boolean empty) {
		super.updateItem(item, empty);

		if (!empty) {
			HBox box = new HBox();
			box.setAlignment(Pos.CENTER);
			box.getStyleClass().addAll("space5");

			Button plus = new Button("+");
			Button minus = new Button("-");

			Label qty = new Label();
			qty.setMinWidth(20);
			qty.setAlignment(Pos.CENTER);
			qty.getStyleClass().add("text-fill-inverse");
			qty.setText(item.toString());

			plus.setOnAction(e -> handler.accept(o -> {
				o.setQuantity(o.getQuantity() + 1);
				o.setTotalPrice(o.getItem().getPrice() * o.getQuantity());
			}, getIndex()));

			minus.setOnAction(e -> handler.accept(o -> {
				o.setQuantity(o.getQuantity() > 1 ? o.getQuantity() - 1 : 1);
				o.setTotalPrice(o.getItem().getPrice() * o.getQuantity());
			}, getIndex()));

			this.setStyle("-fx-padding: 0");
			box.getChildren().addAll(minus, qty, plus);
			box.getChildren().stream().filter(n -> n instanceof Button).forEach(n -> {
				((Button) n).setMinWidth(30);
				((Button) n).setMaxWidth(30);
				((Button) n).setPadding(new Insets(0));
				((Button) n).getStyleClass().addAll("background-radius-50", "color-700", "text-fill-default",
						"cursor-hand");
			});

			setGraphic(box);
			setText(null);

		} else {
			setGraphic(null);
			setText(null);
		}
	}

}
