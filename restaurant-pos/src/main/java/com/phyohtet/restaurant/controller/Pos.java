package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.controller.subscene.NewOrder;
import com.phyohtet.restaurant.entity.OrderDetail;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.OrderItem;
import com.phyohtet.restaurant.service.OrderDetailService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.util.OrderHolder;
import com.phyohtet.restaurant.view.util.TableNameCell;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Controller
@Scope("prototype")
public class Pos implements Initializable {

	@FXML
	private TableView<OrderDetail> orderList;
	@FXML
	private TableColumn<OrderDetail, OrderHead> orderHeadColumn;
	@FXML
	private TableView<OrderItem> checkOutTable;
	@FXML
	private TableColumn<OrderItem, String> nameColumn;
	@FXML
	private TextField totalPrice;
	@FXML
	private TextField totalItem;
	@FXML
	private TextField yourMoney;
	@FXML
	private TextField change;
	@FXML
	private Label checkHead;

	@Autowired
	private OrderDetailService odService;
	private OrderDetail orderDetail;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderList.getItems().addAll(OrderHolder.getInstance().getOrderDetails());
		orderHeadColumn.setCellFactory(c -> new TableNameCell<>());
		orderHeadColumn.getStyleClass().addAll("header-left15");

		initCheckOutTable();

		yourMoney.textProperty().addListener((a, b, c) -> calculateChange(c));
	}

	private void calculateChange(String value) {
		try {
			int money = Integer.parseInt(value) - Integer.parseInt(totalPrice.getText());
			change.setText(String.valueOf(money));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initCheckOutTable() {
		nameColumn.setCellFactory(c -> new TableNameCell<>());
		nameColumn.getStyleClass().addAll("header-left15");
	}

	public void newOrder() {
		try {
			NewOrder.show(s -> {
				OrderHolder.getInstance().add(s);
				orderList.getItems().add(s);
			});
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}

	}

	public void remove() {
		OrderDetail orderDetail = orderList.getSelectionModel().getSelectedItem();
		if (checkSelection(orderDetail) && MessageUtil.showConfirmDialog("Confirm your delete!")) {
			OrderHolder.getInstance().remove(orderDetail);
			orderList.getItems().remove(orderDetail);
		}
	}

	public void edit() {
		OrderDetail orderDetail = orderList.getSelectionModel().getSelectedItem();
		if (checkSelection(orderDetail)) {
			NewOrder.show(orderDetail, s -> {
				OrderHolder.getInstance().replace(s);
				orderList.refresh();
			});
		}
	}

	public void checkout() {
		orderDetail = orderList.getSelectionModel().getSelectedItem();
		if (checkSelection(orderDetail)) {
			checkOutTable.getItems().clear();
			checkOutTable.getItems().addAll(orderDetail.getOrderItems());
			totalPrice.setText(String.valueOf(orderDetail.getTotalPrice()));
			totalItem.setText(String.valueOf(orderDetail.getTotalItem()));
			checkHead.setText(orderDetail.getOrderHead().getName());
		}
	}

	public void clearCheckOut() {
		totalPrice.setText("0");
		totalItem.setText("0");
		yourMoney.setText("0");
		change.setText("0");
		checkHead.setText(null);
		checkOutTable.getItems().clear();
	}

	public void paid() {
		try {
			odService.create(orderDetail);
			orderList.getItems().remove(orderDetail);
			OrderHolder.getInstance().remove(orderDetail);
			orderDetail = null;
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}

	public void print() {

	}

	private boolean checkSelection(OrderDetail orderDetail) {
		if (null != orderDetail) {
			return true;
		}
		MessageUtil.showDialog("Please select one!", AlertType.WARNING);
		return false;
	}
}
