package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.controller.subscene.ItemForm;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.CategoryService;
import com.phyohtet.restaurant.service.ItemService;
import com.phyohtet.restaurant.service.TypeService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.util.MessageUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Controller
@Scope("prototype")
public class Items implements Initializable {

	@FXML
	private TableView<Item> itemsTable;
	@FXML
	private ComboBox<Category> sCategory;
	@FXML
	private ComboBox<Type> sType;
	@FXML
	private TextField sName;

	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoryService catService;
	@Autowired
	private TypeService typeService;

	private BackgroundLoaderService<List<Item>> loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));

		sCategory.getItems().addAll(catService.findAll());
		sType.getItems().addAll(typeService.findAll());
		
		sName.textProperty().addListener((a, b, c) -> search());
		sType.valueProperty().addListener((a, b, c) -> search());
		sCategory.valueProperty().addListener((a, b, c) -> search());

		loader.start();
	}

	public void add() {
		ItemForm.show(null, this::search);
	}

	public void delete() {
		try {
			Item item = itemsTable.getSelectionModel().getSelectedItem();
			if (checkSelection(item) && MessageUtil.showConfirmDialog("Confirm your delete!")) {
				item.setDeleted(true);
				itemService.delete(item);
				search();
			}
		} catch (Exception e) {
			MessageUtil.showDialog("Cannot delete item!", AlertType.ERROR);
		}
	}

	public void edit() {
		Item item = itemsTable.getSelectionModel().getSelectedItem();
		if (checkSelection(item)) {
			ItemForm.show(item, this::search);
		}
	}

	private boolean checkSelection(Item item) {
		if (null != item) {
			return true;
		}
		MessageUtil.showDialog("Please select an item!", AlertType.WARNING);
		return false;
	}

	public void clear() {
		sCategory.setValue(null);
		sType.setValue(null);
		sName.setText(null);
	}

	private synchronized List<Item> call() {
		return itemService.findBy(sCategory.getValue(), sType.getValue(), sName.getText());
	}

	private void showData(List<Item> items) {
		itemsTable.getItems().clear();
		itemsTable.getItems().addAll(items);
	}

	public void search() {
		loader.restart();
	}
}
