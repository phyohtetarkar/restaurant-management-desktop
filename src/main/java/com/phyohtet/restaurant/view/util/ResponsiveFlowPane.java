package com.phyohtet.restaurant.view.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class ResponsiveFlowPane extends FlowPane {

	public ResponsiveFlowPane() {		
		this.widthProperty().addListener(this::widthChanged);
	}
	
	private void widthChanged(ObservableValue<? extends Number> ob, Number ov, Number nv) {
		refreshChild(nv);
	}
	
	public void refreshChild(Number width) {
		getChildren().stream()
			.filter(n -> n instanceof Pane)
			.forEach(n -> {
				changeWidth(width.doubleValue(), n);
			});
	}
	
	private void changeWidth(double width, Node node) {
		if (width >= 1280) {
			((Pane) node).setPrefWidth((width - 75) / 6);
		} else if (width >= 830) {
			((Pane) node).setPrefWidth((width - 65) / 5);
		} else if (width >= 630) {
			((Pane) node).setPrefWidth((width - 55) / 4);
		} else if (width >= 530) {
			((Pane) node).setPrefWidth((width - 45) / 3);
		} else if (width >= 430) {
			((Pane) node).setPrefWidth((width - 35) / 2);
		} else {
			((Pane) node).setPrefWidth((width - 20));
		}
	}
}
