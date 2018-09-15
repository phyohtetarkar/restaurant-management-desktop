package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.service.OrderHeadService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.view.custom.OrderHeadBox;
import com.phyohtet.restaurant.view.util.ResponsiveFlowPane;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

@Controller
@Scope("prototype")
public class OrderHeads implements Initializable {

	@FXML
	private ResponsiveFlowPane pane;
	@FXML
	private TextField name;

	@Autowired
	private OrderHeadService service;
	
	private BackgroundLoaderService<List<OrderHead>> loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));

		loader.start();
	}

	public void add() {
		try {
			OrderHead orderHead = new OrderHead();
			orderHead.setName(name.getText());

			service.save(orderHead);

			name.setText(null);

			reload();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}

	}

	private void updateOrderHead(OrderHead orderHead) {
		try {
			service.save(orderHead);
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}

	private void deleteOrderHead(OrderHead orderHead) {
		try {
			orderHead.setDeleted(true);
			service.save(orderHead);
			reload();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);

		}
	}

	private void reload() {
		loader.restart();
	}

	private synchronized List<OrderHead> call() {
		return service.findAll();
	}

	private void showData(List<OrderHead> orderHead) {
		pane.getChildren().clear();
		pane.getChildren()
				.addAll(orderHead.stream().map(c -> new OrderHeadBox(c, this::updateOrderHead, this::deleteOrderHead))
						.collect(Collectors.toList()));

		pane.refreshChild(pane.getWidth());
	}
}
