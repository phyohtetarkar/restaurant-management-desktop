package com.phyohtet.restaurant.controller.subscene;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.OrderItem;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.OrderItemService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.view.util.TableNameCell;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Controller
@Scope("prototype")
public class AllReport implements Initializable {

	@FXML
	private TableView<OrderItem> table;
	@FXML
	private TableColumn<OrderItem, OrderHead> orderHeadColumn;
	@FXML
	private TableColumn<OrderItem, Item> itemColumn;
	@FXML
	private TableColumn<OrderItem, Date> dateColumn;

	private OrderHead sOrderHead;
	private Category sCategory;
	private Type sType;
	private LocalDate sDate;
	private String sName;

	@Autowired
	private OrderItemService service;

	private BackgroundLoaderService<List<OrderItem>> loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));

		orderHeadColumn.setCellFactory(c -> new TableNameCell<>());
		orderHeadColumn.getStyleClass().addAll("header-left15");

		itemColumn.setCellFactory(c -> new TableNameCell<>());
		itemColumn.getStyleClass().addAll("header-left15");

		loader.start();
	}

	private synchronized List<OrderItem> call() {
		return service.findBy(sOrderHead, sCategory, sType, sDate, sName);
	}

	private void showData(List<OrderItem> orderDetails) {
		System.out.println(orderDetails);
		table.getItems().clear();
		table.getItems().addAll(orderDetails);
	}

	public void search(OrderHead sOrderHead, Category sCategory, Type sType, LocalDate sDate, String sName) {
		this.sOrderHead = sOrderHead;
		this.sCategory = sCategory;
		this.sType = sType;
		this.sDate = sDate;
		this.sName = sName;

		loader.restart();
	}
}
