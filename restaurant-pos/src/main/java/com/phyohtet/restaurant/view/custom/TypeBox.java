package com.phyohtet.restaurant.view.custom;

import java.util.function.Consumer;

import com.phyohtet.restaurant.entity.Type;

public class TypeBox extends NameBox<Type> {

	public TypeBox(Type entity, Consumer<Type> upListener, Consumer<Type> delListener) {
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
