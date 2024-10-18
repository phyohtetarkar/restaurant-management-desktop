package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.service.CategoryService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.view.custom.CategoryBox;
import com.phyohtet.restaurant.view.util.ResponsiveFlowPane;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

@Controller
@Scope("prototype")
public class Categories implements Initializable {

	@FXML
	private ResponsiveFlowPane pane;
	@FXML
	private TextField name;

	@Autowired
	private CategoryService service;

	private BackgroundLoaderService<List<Category>> loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));

		loader.start();
	}

	private void updateCategory(Category category) {
		try {
			service.save(category);
		} catch (ApplicationException e) {
			e.printStackTrace();
			MessageUtil.handle(e);
		}

	}

	private void deleteCategory(Category category) {
		try {
			category.setDeleted(true);
			service.save(category);
			reload();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}

	public void add() {
		try {
			Category category = new Category();
			category.setName(name.getText());

			service.save(category);
			name.setText(null);
			reload();
		} catch (ApplicationException e) {
			e.printStackTrace();
			MessageUtil.handle(e);
		}

	}

	private void reload() {
		loader.restart();
	}

	private synchronized List<Category> call() {
		return service.findAll();
	}

	private void showData(List<Category> categories) {
		pane.getChildren().clear();
		pane.getChildren()
				.addAll(categories.stream().map(c -> new CategoryBox(c, this::updateCategory, this::deleteCategory))
						.collect(Collectors.toList()));

		pane.refreshChild(pane.getWidth());
	}
}
