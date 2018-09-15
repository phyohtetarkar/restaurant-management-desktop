package com.phyohtet.restaurant.view.custom;

import java.util.function.Consumer;

import com.phyohtet.restaurant.entity.Category;

public class CategoryBox extends NameBox<Category> {

	public CategoryBox(Category entity, Consumer<Category> upListener, Consumer<Category> delListener) {
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
