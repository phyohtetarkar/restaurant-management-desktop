package com.phyohtet.restaurant.view.util;

import java.util.function.Consumer;

import com.phyohtet.restaurant.util.SvgContainer;

import javafx.scene.control.TableCell;
import javafx.scene.shape.SVGPath;

public class DeleteCell<T> extends TableCell<T, String> {

	private Consumer<Integer> handler;

	public DeleteCell(Consumer<Integer> handler) {
		this.handler = handler;
	}

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (!empty) {

			SVGPath path = new SVGPath();
			path.setContent(SvgContainer.DEL_SVG.getValue());
			path.getStyleClass().addAll("cursor-hand");
			path.setOnMouseClicked(e -> handler.accept(getIndex()));

			setGraphic(path);
			setText(null);

		} else {
			setGraphic(null);
			setText(null);
		}
	}
}
