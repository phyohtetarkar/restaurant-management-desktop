package com.phyohtet.restaurant.controller.subscene;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.controller.subscene.ItemForm.DataLoader;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.ItemService;
import com.phyohtet.restaurant.service.TypeService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.util.SpringContextManager;
import com.phyohtet.restaurant.view.util.TableComboCell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

@Controller
@Scope("prototype")
public class AddMoreItem implements Initializable {

	@FXML
	private TableView<Item> table;
	@FXML
	private TableColumn<Item, String> nameCol;
	@FXML
	private TableColumn<Item, Type> typeCol;
	@FXML
	private TableColumn<Item, Integer> priceCol;
	@FXML
	private TableColumn<Item, Integer> taxCol;
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private TypeService typeService;

	private Consumer<Item> consumer;
	private ContextMenu contextMenu;
	private DataLoader loader;
	private Item item;
	
	public static void show(Item item, Consumer<Item> consumer, DataLoader loader) {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);

			ConfigurableApplicationContext context = SpringContextManager.getContext();
			FXMLLoader fxml = new FXMLLoader(AddMoreItem.class.getResource("ADDMOREITEM.fxml"));
			fxml.setControllerFactory(context::getBean);
			Parent root = fxml.load();
			stage.setScene(new Scene(root));

			AddMoreItem controller = fxml.getController();
			controller.consumer = consumer;
			controller.loader = loader;
			controller.item = item;
			
			List<Type> types = controller.typeService.findAll();
			controller.table.getItems().add(item);
			
			controller.typeCol.setCellFactory(c -> new TableComboCell(types, (co, i) -> {
				co.accept(controller.table.getItems().get(i));
				controller.table.refresh();
			}));

			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		taxCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		
		nameCol.setOnEditCommit(evt -> evt.getRowValue().setName(evt.getNewValue()));
		
		priceCol.setOnEditCommit(evt -> {
			try {
				evt.getRowValue().setPrice(evt.getNewValue());
			} catch (Exception e) {
				e.printStackTrace();
				evt.getRowValue().setPrice(evt.getOldValue());
			}
		});

		taxCol.setOnEditCommit(evt -> {
			try {
				evt.getRowValue().setTax(evt.getNewValue());
			} catch (Exception e) {
				e.printStackTrace();
				evt.getRowValue().setTax(evt.getOldValue());
			}
		});
		
		initMenu();
		
		table.setContextMenu(contextMenu);
		
	}

	private void initMenu() {
		contextMenu = new ContextMenu();
		contextMenu.getStyleClass().add("context-menu");
		MenuItem duplicate = new MenuItem("Duplicate");
		MenuItem remove = new MenuItem("Remove");
		contextMenu.getItems().addAll(duplicate, remove);
		
		duplicate.setOnAction(evt -> {
			try {
				Item item = (Item) table.getSelectionModel().getSelectedItem().clone();
				item.setId(0);
				table.getItems().add(item);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
		
		remove.setOnAction(evt -> {
			try {
				if (table.getSelectionModel().getSelectedItem() == item) {
					throw new ApplicationException("Can't Remove Original Item!");
				} else {
					table.getItems().remove(table.getSelectionModel().getSelectedIndex());
				}
			} catch (ApplicationException e) {
				MessageUtil.handle(e);
			}
			
		});
		
	}

	public void save() {
		try {
			table.getItems().forEach(item -> {
				if (item.getId() > 0) {
					itemService.update(item);
				} else {
					itemService.create(item);
				}
				
			});
			loader.load();
			table.getScene().getWindow().hide();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
		
	}

	public void back() {
		table.getScene().getWindow().hide();
		consumer.accept(table.getItems().get(0));
	}

}
