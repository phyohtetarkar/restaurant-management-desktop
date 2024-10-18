package com.phyohtet.restaurant.controller.subscene;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.OrderDetail;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.OrderItem;
import com.phyohtet.restaurant.service.OrderDetailService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.view.util.TableNameCell;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Controller
@Scope("prototype")
public class DetailReport implements Initializable {

	@FXML
	private TableView<OrderDetail> saleTable;
	@FXML
	private TableColumn<OrderDetail, OrderHead> orderHeadColumn;
	@FXML
	private TableView<OrderItem> itemTable;
	@FXML
	private TableColumn<OrderItem, Item> nameColumn;

	private OrderHead sOrderHead;
	private LocalDate sDate;

	@Autowired
	private OrderDetailService service;
	
	private BackgroundLoaderService<List<OrderDetail>> loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));
		
		saleTable.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> handleTableSelection(c));

		orderHeadColumn.setCellFactory(c -> new TableNameCell<>());
		orderHeadColumn.getStyleClass().add("header-left15");

		nameColumn.setCellFactory(c -> new TableNameCell<>());
		nameColumn.getStyleClass().add("header-left15");

		loader.start();
	}

	private void handleTableSelection(OrderDetail od) {
		itemTable.getItems().clear();
		itemTable.getItems().addAll(od.getOrderItems());
	}

	private synchronized List<OrderDetail> call() {
		return service.findBy(sOrderHead, sDate);
	}

	private void showData(List<OrderDetail> ordetDetails) {
		saleTable.getItems().clear();
		saleTable.getItems().addAll(ordetDetails);
	}

	public void search(OrderHead sOrderHead, LocalDate sDate) {
		this.sOrderHead = sOrderHead;
		this.sDate = sDate;

		loader.restart();
	}
}
