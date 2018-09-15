package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.util.Navigator;
import com.phyohtet.restaurant.util.SvgContainer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

public class ParentView implements Initializable {

	@FXML
	private StackPane contentView;
	@FXML
	private ToggleGroup menu;
	@FXML
	private Label loading;
	@FXML
	private Label error;
	@FXML
	private Label title;
	
	private ContextMenu contextMenu;
	private boolean selected;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Navigator.setContentView(contentView);
		Navigator.setLoadingView(loading);
		Navigator.setTitle(title);
		Navigator.navigate("HOME");
		MessageUtil.init(error);
		
		initContextMenu();
		
		menu.selectedToggleProperty().addListener((a, b, c) -> {
			if (c != null) {
				if (((ToggleButton) c).getText().equals("Master")) {
					if (!contextMenu.isShowing()) {
						contextMenu.show(((ToggleButton) c), Side.BOTTOM, 5, 3);
						if (b != null) {
							b.setSelected(true);
						}
					} else {
						b.setSelected(!selected);
					}
				} else {
					if (!contextMenu.isShowing()) {
						Navigator.navigate(((ToggleButton) c).getText());
					}
				}
			} else if (b != null) {
				b.setSelected(true);
			}
		});
	}

	private void initContextMenu() {
		contextMenu = new ContextMenu();
		
		SVGPath pathItem = new SVGPath();
		pathItem.setContent(SvgContainer.ITEM_SVG.getValue());
		SVGPath pathOrderHead = new SVGPath();
		pathOrderHead.setContent(SvgContainer.HEAD_SVG.getValue());
		SVGPath pathCategory = new SVGPath();
		pathCategory.setContent(SvgContainer.CATEGORY_SVG.getValue());
		SVGPath pathType = new SVGPath();
		pathType.setContent(SvgContainer.TYPE_SVG.getValue());
		
		MenuItem items = new MenuItem("Items", pathItem);
		items.setOnAction(e -> Navigator.navigate(items.getText()));
		MenuItem categories = new MenuItem("Categories", pathCategory);
		categories.setOnAction(e -> Navigator.navigate(categories.getText()));
		MenuItem orderHeads = new MenuItem("OrderHeads", pathOrderHead);
		orderHeads.setOnAction(e -> Navigator.navigate(orderHeads.getText()));
		MenuItem types = new MenuItem("Types", pathType);
		types.setOnAction(e -> Navigator.navigate(types.getText()));
		
		contextMenu.getItems().addAll(items, categories, orderHeads, types);
		contextMenu.setOnAction(e -> {
			selected = true;
			((ToggleButton)contextMenu.getOwnerNode()).setSelected(true);
			selected = false;
		});
	}

	public void close() {
		Platform.exit();
	}
}
