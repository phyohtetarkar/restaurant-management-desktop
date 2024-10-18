package com.phyohtet.restaurant.view.custom;

import java.util.function.Consumer;

import com.phyohtet.restaurant.entity.OrderHead;

public class OrderHeadBox extends NameBox<OrderHead> {

	public OrderHeadBox(OrderHead entity, Consumer<OrderHead> upListener, Consumer<OrderHead> delListener) {
		super(entity, upListener, delListener);
		name.setText(entity.getName());
		text.setText(entity.getName());
	}

	@Override
	protected void handleUpdate() {
		entity.setName(text.getText());
		upListener.accept(entity);
		name.setText(entity.getName());
	}

}
