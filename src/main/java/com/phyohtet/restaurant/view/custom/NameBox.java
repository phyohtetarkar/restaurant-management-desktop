package com.phyohtet.restaurant.view.custom;

import java.util.function.Consumer;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.util.MessageUtil;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class NameBox<T> extends HBox {

	protected Label id;
	protected Label name;
	protected TextField text;
	protected Consumer<T> upListener;
	private Consumer<T> delListener;
	protected T entity;

	private ContextMenu menu;

	public NameBox(T entity, Consumer<T> upListener, Consumer<T> delListener) {
		this.entity = entity;
		this.upListener = upListener;
		this.delListener = delListener;

		initMenu();

		id = new Label();
		name = new Label();
		text = new TextField();
		text.setOnKeyPressed(this::handleTextField);
		text.focusedProperty().addListener((a, b, c) -> {
			if (!c) {
				switchToView();
			}
		});

		setOnMouseClicked(this::handleBox);

		setHgrow(text, Priority.ALWAYS);
		getStyleClass().addAll("data-box", "color-700", "shadowSolid");
		getChildren().addAll(id, name);
	}

	private void handleTextField(KeyEvent e) {
		switch (e.getCode()) {
		case ENTER:
			try {
				handleUpdate();
				getChildren().remove(text);
			} catch (ApplicationException e2) {
				text.setText(name.getText());
				MessageUtil.handle(e2);
			}
			break;
		case ESCAPE:
			getChildren().remove(text);
			break;

		default:
			break;
		}
	}

	private void handleBox(MouseEvent e) {
		menu.hide();
		if (e.getButton() == MouseButton.SECONDARY) {
			menu.show(this, e.getScreenX(), e.getScreenY());
		} else {
			if (!getChildren().contains(text)) {
				switchToEdit();
			}
		}
	}

	private void initMenu() {
		MenuItem item = new MenuItem("Delete");
		menu = new ContextMenu();
		menu.getItems().add(item);
		menu.getStyleClass().addAll("white-background", "radius-2");
		menu.setOnAction(e -> {
			delListener.accept(entity);
		});
	}

	private void switchToEdit() {
		getChildren().remove(id);
		getChildren().remove(name);
		getChildren().add(text);
		text.requestFocus();
	}

	private void switchToView() {
		getChildren().remove(text);
		getChildren().add(id);
		getChildren().add(name);
	}

	protected abstract void handleUpdate();

}
