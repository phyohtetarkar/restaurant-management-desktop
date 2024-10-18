package com.phyohtet.restaurant.view.util;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.Type;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;

public class TableComboCell extends TableCell<Item, Type> {
	
	private List<Type> types;
	private BiConsumer<Consumer<Item>, Integer> consumer;
	
	public TableComboCell(List<Type> types, BiConsumer<Consumer<Item>, Integer> consumer) {
		this.types = types;
		this.consumer = consumer;
	}

	@Override
	protected void updateItem(Type type, boolean empty) {
		super.updateItem(type, empty);
		
		if (null != type && !empty) {
			ComboBox<Type> combo = new ComboBox<>();
			combo.getStyleClass().addAll("shadow", "color-300", "customize-combo");
			combo.setPrefWidth(120);
			combo.getItems().addAll(types);
			combo.setValue(type);
			combo.valueProperty().addListener((a, b, c) -> consumer.accept(item -> {
				item.setType(c);
			}, getIndex()));
			
			setGraphic(combo);
		} else {
			setGraphic(null);
		}
	}
}
