package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.TypeService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.view.custom.TypeBox;
import com.phyohtet.restaurant.view.util.ResponsiveFlowPane;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

@Controller
@Scope("prototype")
public class Types implements Initializable {

	@FXML
	private ResponsiveFlowPane pane;
	@FXML
	private TextField name;

	@Autowired
	private TypeService service;

	private BackgroundLoaderService<List<Type>> loader;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));
		
		loader.start();
	}

	private void updateType(Type type) {
		try {
			service.save(type);
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}

	}

	private void deleteType(Type type) {
		type.setDeleted(true);
		service.save(type);
		reload();
	}

	public void add() {
		try {
			Type type = new Type();
			type.setName(name.getText());
			
			service.save(type);

			name.setText(null);

			reload();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}

	private void reload() {
		loader.restart();
	}

	private synchronized List<Type> call() {
		return service.findAll();
	}

	private void showData(List<Type> types) {
		pane.getChildren().clear();
		pane.getChildren().addAll(types.stream().map(t -> new TypeBox(t, this::updateType, this::deleteType))
				.collect(Collectors.toList()));

		pane.refreshChild(pane.getWidth());
	}
}
