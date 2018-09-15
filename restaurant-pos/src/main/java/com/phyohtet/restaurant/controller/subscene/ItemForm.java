package com.phyohtet.restaurant.controller.subscene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.CategoryService;
import com.phyohtet.restaurant.service.ItemService;
import com.phyohtet.restaurant.service.TypeService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.util.SpringContextManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Controller
@Scope("prototype")
public class ItemForm implements Initializable {

	private static final String EDIT_HEADER = "Update Item";
	private static final String ADD_HEADER = "Add New Item";

	@FXML
	private Label header;
	@FXML
	private TextField name;
	@FXML
	private TextField price;
	@FXML
	private TextField tax;
	@FXML
	private ComboBox<Category> categories;
	@FXML
	private ListView<Type> types;
	@FXML
	private TextArea description;

	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoryService catService;
	@Autowired
	private TypeService typeService;

	private Item item;
	private DataLoader loader;
	private static Stage stage;

	public interface DataLoader {
		void load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categories.getItems().addAll(catService.findAll());
		types.getItems().addAll(typeService.findAll());

		price.textProperty().addListener((a, b, c) -> {
			if (!c.matches("\\d*")) {
				price.setText(c.replaceAll("[^\\d]", ""));
			}
		});
		tax.textProperty().addListener((a, b, c) -> {
			if (!c.matches("\\d*")) {
				tax.setText(c.replaceAll("[^\\d]", ""));
			}
		});

	}

	public static void show(Item item, DataLoader loader) {
		try {
			stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);

			ConfigurableApplicationContext context = SpringContextManager.getContext();
			FXMLLoader fxml = new FXMLLoader(ItemForm.class.getResource("ITEMFORM.fxml"));
			fxml.setControllerFactory(context::getBean);
			Parent root = fxml.load();
			stage.setScene(new Scene(root));

			ItemForm controller = fxml.getController();
			controller.loader = loader;

			if (null != item) {
				controller.setItem(item);
				controller.header.setText(EDIT_HEADER);
			} else {
				controller.header.setText(ADD_HEADER);
			}

			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			if (null != this.item) {
				itemService.update(buildItem());
			} else {
				itemService.create(buildItem());
			}

			loader.load();
			close();

		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}

	}
	
	public void more() {
		try {
			itemService.validate(buildItem());
			AddMoreItem.show(item, it -> {
				price.setText(String.valueOf(it.getPrice()));
				tax.setText(String.valueOf(it.getTax()));
				types.getSelectionModel().select(it.getType());
				stage.show();
			}, loader);
			stage.hide();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}
	
	public void close() {
		name.getScene().getWindow().hide();
	}
	
	private Item buildItem() {
		if (null == item) {
			item = new Item();
		}
		item.setName(name.getText());
		item.setPrice(price.getText().isEmpty() ? 0 : Integer.parseInt(price.getText()));
		item.setTax(tax.getText().isEmpty() ? 0 : Integer.parseInt(tax.getText()));
		item.setCategory(categories.getValue());
		item.setType(types.getSelectionModel().getSelectedItem());
		item.setDescription(description.getText());
		
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
		categories.setValue(item.getCategory());
		name.setText(item.getName());
		price.setText(String.valueOf(item.getPrice()));
		tax.setText(String.valueOf(item.getTax()));
		types.getSelectionModel().select(item.getType());
		description.setText(item.getDescription());
	}

}
